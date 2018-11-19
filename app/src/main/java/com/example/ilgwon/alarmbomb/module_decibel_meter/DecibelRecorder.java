package com.example.ilgwon.alarmbomb.module_decibel_meter;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;

public class DecibelRecorder {
    public File myRecAudioFile;
    private MediaRecorder mMediaRecorder;
    public boolean isRecording = false;


    /**
     * get MaxAmplitude
     *
     * @return return getMaxAmplitude
     */
    public float getMaxAmplitude() {
        if (mMediaRecorder != null) {
            try {
                return mMediaRecorder.getMaxAmplitude();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return 0;
            }
        } else {
            return 5;
        }
    }

//    public File getMyRecAudioFile() {
//        return myRecAudioFile;
//    }

    public void setMyRecAudioFile(File myRecAudioFile) {
        this.myRecAudioFile = myRecAudioFile;
    }

    /**
     * Start Recording
     *
     * @return Starting recording successfully or not
     */
    public boolean startRecorder() {
        if (myRecAudioFile == null) {
            return false;
        }
        try {
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setOutputFile(myRecAudioFile.getAbsolutePath());
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            isRecording = true;
            return true;
        } catch (IOException exception) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            isRecording = false;
            exception.printStackTrace();
        } catch (IllegalStateException e) {
            stopRecording();
            e.printStackTrace();
            isRecording = false;
        }
        return false;
    }

    /**
     * Stop Recording
     *
     * @return Stopping recording successfully or not
     */
    public void stopRecording() {
        if (mMediaRecorder != null) {
            if (isRecording) {
                try {
                    mMediaRecorder.stop();
                    mMediaRecorder.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mMediaRecorder = null;
            isRecording = false;
        }
    }


    /**
     * Delete Recording
     *
     * @return Deleting recording
     */
    public void delete() {
        // Stop recording and remove the file.
        stopRecording();
        if (myRecAudioFile != null) {
            myRecAudioFile.delete();
            myRecAudioFile = null;

        }
    }
}