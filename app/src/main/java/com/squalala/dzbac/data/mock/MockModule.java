package com.squalala.dzbac.data.mock;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : MockItemModule.java
 * Date : 11 juil. 2014
 * 
 */
@Module
public class MockModule {
	
	@Provides @Singleton
	MockPostLoader provideMockAnnonceLoader() {
		return new MockPostLoader();
	}
	
}
