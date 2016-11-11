/***
 * 7  Copyright (c) 2013 CommonsWare, LLC
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.roxwin.imagefilter.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.commonsware.cwac.camera.CameraFragment;
import com.commonsware.cwac.camera.PictureTransaction;
import com.commonsware.cwac.camera.SimpleCameraHost;
import com.roxwin.imagefilter.R;
import com.roxwin.imagefilter.activities.DisplayActivity;

public class FrontCameraFragment extends CameraFragment implements
        OnSeekBarChangeListener {
    static FrontCameraFragment newInstance() {
        FrontCameraFragment f = new FrontCameraFragment();
        return (f);
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        setHasOptionsMenu(true);

        SimpleCameraHost.Builder builder =
                new SimpleCameraHost.Builder(new DemoCameraHost(getActivity()));

        setCameraHost(builder.useFullBleedPreview(true).build());

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View cameraView =
                super.onCreateView(inflater, container, savedInstanceState);
        View results = inflater.inflate(R.layout.fragment_camera, container, false);

        ((ViewGroup) results.findViewById(R.id.camera)).addView(cameraView);
        cameraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeSimplePicture();
            }
        });
        lockToPortrait(true);
        return (results);
    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().invalidateOptionsMenu();
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // ignore
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // ignore
    }


    public void takeSimplePicture() {

        PictureTransaction xact = new PictureTransaction(
                getCameraHost());

        takePicture(xact);
    }


    class DemoCameraHost extends SimpleCameraHost {
        public DemoCameraHost(Context _ctxt) {
            super(_ctxt);
        }

        @Override
        public boolean useFrontFacingCamera() {
            return (true);
        }


        @Override
        public void saveImage(PictureTransaction xact, final byte[] image) {

            DisplayActivity.imageToShow = image;
            startActivity(new Intent(getActivity(), DisplayActivity.class));

        }


        @Override
        public void onCameraFail(FailureReason reason) {
            super.onCameraFail(reason);

            Toast.makeText(getActivity(),
                    "Sorry, but you cannot use the camera now!",
                    Toast.LENGTH_LONG).show();
        }
    }
}