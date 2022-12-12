package com.montbleu.OBD.io;

public interface ObdProgressListener {

    void stateUpdate(final ObdCommandJob job);

}