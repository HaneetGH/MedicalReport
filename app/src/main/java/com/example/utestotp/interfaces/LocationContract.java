package com.example.utestotp.interfaces;

import android.location.Location;

public interface LocationContract {
    interface View {
        void setLocation(Location message);
    }

    interface Presenter {
        void bind();

        void unbind();
    }
}
