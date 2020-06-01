package com.example.firestorepractice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.clickEventListener{

    private static final String TAG = "MainActivity";

    public static final String KEY_TITLE = "title";
    public static final String KEY_NOTE = "note";

    TextInputLayout titleInput,noteInput,priorityInput;
    Button addNoteButton,updateNoteButton,sortByPriorityBtn,sortByTitleBtn,greaterThan2,notEqual2,loadMore;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Notebook");
    private ListenerRegistration notebookListener;

    private ArrayList<Note> dataList = new ArrayList<>();
    private NoteAdapter adapter ;

    private Note selectedNote;

    private DocumentSnapshot lastDocument = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleInput = findViewById(R.id.title_input);
        noteInput = findViewById(R.id.note_input);
        addNoteButton = findViewById(R.id.add_note_btn);
        updateNoteButton = findViewById(R.id.update_note_btn);
        priorityInput = findViewById(R.id.priority_input);
        sortByPriorityBtn = findViewById(R.id.sort_by_priority);
        sortByTitleBtn = findViewById(R.id.sort_by_title);
        greaterThan2 = findViewById(R.id.priority_greater_2);
        notEqual2 = findViewById(R.id.priority_not_2);
        loadMore = findViewById(R.id.load_more);

        updateNoteButton.setEnabled(false);

        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNote();
            }
        });

        greaterThan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastDocument = null;
                loadMore.setText("Load");
                greaterThan2();;
            }
        });

        notEqual2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastDocument = null;
                loadMore.setText("Load");
                not2();
            }
        });

        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        updateNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNote();
            }
        });

        sortByPriorityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastDocument = null;
                loadMore.setText("Load");
                sortByPriority();
            }
        });

        sortByTitleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastDocument = null;
                loadMore.setText("Load");
                sortByTitle();
            }
        });



        initializeRecyclerView();
        loadNote();

    }

    private void updateNote() {
        String title = "none";

        if(!titleInput.getEditText().getText().toString().isEmpty()){
            title = titleInput.getEditText().getText().toString();
        }else{
            titleInput.setError("This mustn't be empty");
            return;
        }
        String note = "none";
        if(!noteInput.getEditText().getText().toString().isEmpty()){
            note = noteInput.getEditText().getText().toString();
        }else{
            noteInput.setError("This mustn't be empty");
            return;
        }

        int priority;
        if(!priorityInput.getEditText().getText().toString().isEmpty()){
            priority = Integer.parseInt(priorityInput.getEditText().getText().toString());
        }else{
            priorityInput.setError("This mustn't be empty");
            return;
        }

        selectedNote.setTitle(title);
        selectedNote.setNote(note);
        selectedNote.setPriority(priority);
        notebookRef.document(selectedNote.getId()).set(selectedNote, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this,"Updated",Toast.LENGTH_LONG).show();
                updateNoteButton.setText("Update");
                updateNoteButton.setEnabled(false);
            }
        });
    }


    private void saveNote(){
        String title = "none";

        if(!titleInput.getEditText().getText().toString().isEmpty()){
             title = titleInput.getEditText().getText().toString();
        }else{
            titleInput.setError("This mustn't be empty");
            return;
        }
        String note = "none";
        if(!noteInput.getEditText().getText().toString().isEmpty()){
             note = noteInput.getEditText().getText().toString();
        }else{
            noteInput.setError("This mustn't be empty");
            return;
        }

        int priority;
        if(!priorityInput.getEditText().getText().toString().isEmpty()){
            priority = Integer.parseInt(priorityInput.getEditText().getText().toString());
            Log.i(TAG, "saveNote: "+priority);
        }else{
            priorityInput.setError("This mustn't be empty");
            return;
        }

        Note noteObj = new Note(title,note,priority);

        db.collection("Notebook").document()
                .set(noteObj)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this,"Note saved",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Error!",Toast.LENGTH_LONG).show();
                        Log.e(TAG, "onFailure: ",e);
                    }
                });

        updateNoteButton.setText("Update");
        updateNoteButton.setEnabled(false);
    }


    @Override
    protected void onStart() {
        super.onStart();
        notebookListener = notebookRef.limit(3).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Log.d(TAG, "onEvent: ");
                lastDocument = null;
                dataList.clear();
                if(queryDocumentSnapshots.isEmpty()){
                    adapter.notifyDataSetChanged();
                    return;
                }

                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                    Note note = documentSnapshot.toObject(Note.class);
                    note.setId(documentSnapshot.getId());
                    dataList.add(note);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void loadNote(){

        Query query;
        if(lastDocument == null){
            query = notebookRef.orderBy("priority").limit(3);
        }else{
            query = notebookRef.orderBy("priority").startAfter(lastDocument).limit(3);
        }

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(lastDocument == null){
                    dataList.clear();
                }
                if(queryDocumentSnapshots.isEmpty())
                    return;
                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                    Note note = documentSnapshot.toObject(Note.class);
                    String documentID = documentSnapshot.getId();
                    note.setId(documentID);
                    dataList.add(note);

                }
                adapter.notifyDataSetChanged();
                lastDocument =  queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() -1 );
                loadMore.setText("load more");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Error!",Toast.LENGTH_LONG).show();
                Log.e(TAG, "onFailure: ",e);
            }
        });
    }

    private void initializeRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.note_holder_rv);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NoteAdapter(this,dataList,this);
        recyclerView.setAdapter(adapter);
    }



    @Override
    protected void onStop() {
        super.onStop();
        notebookListener.remove();
    }

    @Override
    public void itemClicked(Note note) {
        selectedNote = note;
        String updateBtnText = "Update no. "+ note.getNo();
        updateNoteButton.setText(updateBtnText);
        updateNoteButton.setEnabled(true);
        titleInput.getEditText().setText(note.getTitle());
        noteInput.getEditText().setText(note.getNote());
        priorityInput.getEditText().setText(note.getNo());
    }

    @Override
    public void itemDeleteClicked(Note note) {
        notebookRef.document(note.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this,"Deleted",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sortByTitle(){
        notebookRef.orderBy("title",Query.Direction.DESCENDING).orderBy("priority", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                dataList.clear();
                if(queryDocumentSnapshots.isEmpty())
                    return;
                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                    Note note = documentSnapshot.toObject(Note.class);
                    String documentID = documentSnapshot.getId();
                    note.setId(documentID);
                    dataList.add(note);

                }
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Error!",Toast.LENGTH_LONG).show();
                Log.e(TAG, "onFailure: ",e);
            }
        });
    }

    private void sortByPriority(){
        notebookRef.orderBy("title",Query.Direction.ASCENDING).orderBy("priority", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                dataList.clear();
                if(queryDocumentSnapshots.isEmpty())
                    return;
                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                    Note note = documentSnapshot.toObject(Note.class);
                    String documentID = documentSnapshot.getId();
                    note.setId(documentID);
                    dataList.add(note);

                }
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Error!",Toast.LENGTH_LONG).show();
                Log.e(TAG, "onFailure: ",e);
            }
        });
    }

    private void greaterThan2(){
        notebookRef.whereGreaterThanOrEqualTo("priority",2).orderBy("priority", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                dataList.clear();
                if(queryDocumentSnapshots.isEmpty())
                    return;
                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                    Note note = documentSnapshot.toObject(Note.class);
                    String documentID = documentSnapshot.getId();
                    note.setId(documentID);
                    dataList.add(note);

                }
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Error!",Toast.LENGTH_LONG).show();
                Log.e(TAG, "onFailure: ",e);
            }
        });
    }

    private void not2(){

        Task task1 = notebookRef.whereLessThan("priority",2).orderBy("priority").get();
        Task task2 = notebookRef.whereGreaterThan("priority",2).orderBy("priority").get();

        Task<List<QuerySnapshot>> allTask = Tasks.whenAllSuccess(task1,task2);
        allTask.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
            @Override
            public void onSuccess(List<QuerySnapshot> querySnapshots) {
                dataList.clear();
                for(QuerySnapshot queryDocumentSnapshots:querySnapshots){
                    for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                        Note note = documentSnapshot.toObject(Note.class);
                        String documentID = documentSnapshot.getId();
                        note.setId(documentID);
                        dataList.add(note);

                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }



}
