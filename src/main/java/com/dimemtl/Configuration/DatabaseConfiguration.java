package com.dimemtl.Configuration;

import com.j256.ormlite.support.ConnectionSource;

public interface DatabaseConfiguration {
    ConnectionSource connectionSource();

}
