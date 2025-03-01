package com.team13.whatsmissing.di;

import com.team13.whatsmissing.data.repository.AssociationsRepository;
import com.team13.whatsmissing.data.repository.RemoteAssociationsRepository;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.FragmentComponent;
import dagger.hilt.android.scopes.FragmentScoped;

@Module
@InstallIn(FragmentComponent.class)
abstract class AssociationsRepositoryModule {

    @Binds
    @FragmentScoped
    public abstract AssociationsRepository bindAssociationsRepository(
            RemoteAssociationsRepository associationsRepository
    );

}
