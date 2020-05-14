package com.example.playstore;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

public class MyLinearSnapHelper extends LinearSnapHelper {

    private static final String TAG = "MyLinearSnapHelper";

    @Nullable
    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
        int[] out = new int[2];
        if (layoutManager.canScrollHorizontally()) {
            out[0] = getDistance(layoutManager, targetView, OrientationHelper.createHorizontalHelper(layoutManager));
        } else {
            out[0] = 0;
        }

        if (layoutManager.canScrollVertically()) {
            out[1] = getDistance(layoutManager, targetView, OrientationHelper.createVerticalHelper(layoutManager));
        } else {
            out[1] = 0;
        }
        return out;
    }

    @Nullable
    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {

            if (layoutManager.canScrollHorizontally()) {
                return getStartView(layoutManager, OrientationHelper.createHorizontalHelper(layoutManager));
            } else {
                return getStartView(layoutManager, OrientationHelper.createVerticalHelper(layoutManager));
            }
        }

        return super.findSnapView(layoutManager);
    }


    int getDistance(RecyclerView.LayoutManager layoutManager, View targetView, OrientationHelper helper) {
        final int childStart = helper.getDecoratedStart(targetView);
        final int containerStart = helper.getStartAfterPadding();
        return childStart - containerStart;
    }


    private View getStartView(RecyclerView.LayoutManager layoutManager,
                              OrientationHelper helper) {

        if (layoutManager instanceof LinearLayoutManager) {
            int firstChild = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();

            boolean isLastItem = ((LinearLayoutManager) layoutManager)
                    .findLastCompletelyVisibleItemPosition()
                    == layoutManager.getItemCount() - 1;

            if (firstChild == RecyclerView.NO_POSITION) {
                return null;
            }

            View child = layoutManager.findViewByPosition(firstChild);
            Log.d(TAG, "getStartView: "+ helper.getDecoratedEnd(child)+" "+helper.getDecoratedMeasurement(child)+" "+firstChild+" "+isLastItem);
            if (helper.getDecoratedEnd(child) >= helper.getDecoratedMeasurement(child) / 2
                    && helper.getDecoratedEnd(child) > 0) {
                return child;
            } else {
                if (((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition()
                        == layoutManager.getItemCount() - 1) {
                    return null;
                } else {
                    return layoutManager.findViewByPosition(firstChild + 1);
                }
            }


        }



        return super.findSnapView(layoutManager);
    }
}
