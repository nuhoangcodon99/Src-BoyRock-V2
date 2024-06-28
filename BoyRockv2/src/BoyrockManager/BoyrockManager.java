/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BoyrockManager;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;

import com.boyrock.jdbc.daos.PlayerDAO;
import com.boyrock.server.Client;


/**
 *
 * @author Lucy An Trom
 */
public class BoyrockManager {
    private static BoyrockManager instance = null;
    
    private BoyrockManager() {
        compositeDisposable = new CompositeDisposable();
    }

    // Static method
    // Static method to create instance of Singleton class
    public static synchronized BoyrockManager getInstance() {
        if (instance == null) {
            instance = new BoyrockManager();
        }
        return instance;
    }
    
    private CompositeDisposable compositeDisposable;
    
    public void autoSave() {
        System.out.println("[AutoSaveManager] start autosave");
        Disposable subscribe = Observable.interval(60, 90, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .subscribe(i -> {
                    this.handleAutoSave();
                },  throwable -> {
              System.out.println("[AutoSaveManager] start autosave error: " + throwable.getLocalizedMessage());
        });
        compositeDisposable.add(subscribe);               
    }
    
    public void handleAutoSave() {
        Client.gI().getPlayers().forEach(player -> {
            PlayerDAO.updatePlayer(player);
        });
    }
    
    private void dispose() {
        compositeDisposable.dispose();
        compositeDisposable = null;
    }
}
