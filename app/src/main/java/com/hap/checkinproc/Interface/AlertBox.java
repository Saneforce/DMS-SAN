package com.hap.checkinproc.Interface;

import android.content.DialogInterface;

public interface AlertBox {
    void PositiveMethod(DialogInterface dialog, int id);
    void NegativeMethod(DialogInterface dialog, int id);

}
