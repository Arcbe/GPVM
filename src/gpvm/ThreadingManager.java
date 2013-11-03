/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Manages thread safety by only allowing one thread to write to
 * the model at a time and no thread may read during this time.  The
 * manager cannot, however, enforce this policy and must be used in order for
 * threads to be safe.
 * 
 * @author russell
 */
public class ThreadingManager {
  public static ThreadingManager getInstance() {
    return instance;
  }
  
  public boolean canWrite() {
    return writing;
  }
  
  public void requestRead() {
    lock.readLock().lock();
  }
  
  public void releaseRead() {
    lock.readLock().unlock();
  }
  
  public void requestWrite() {
    lock.writeLock().lock();
    writing = true;
  }
  
  public void releaseWrite() {
    writing = false;
    lock.writeLock().unlock();
  }
  
  private ThreadingManager() {
    lock = new ReentrantReadWriteLock();
  }
  
  private boolean writing;
  private ReadWriteLock lock;
  
  private static ThreadingManager instance = new ThreadingManager();
}
