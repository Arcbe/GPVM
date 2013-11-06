/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm;

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
    return lock.isWriteLockedByCurrentThread();
  }
  
  public boolean canRead() {
    return lock.getReadHoldCount() > 0;
  }
  
  public void requestRead() {
    lock.readLock().lock();
  }
  
  public void releaseRead() {
    lock.readLock().unlock();
  }
  
  public void requestWrite() {
    lock.writeLock().lock();
  }
  
  public void releaseWrite() {
    lock.writeLock().unlock();
  }
  
  private ThreadingManager() {
    lock = new ReentrantReadWriteLock();
  }
  
  private ReentrantReadWriteLock lock;
  
  private static ThreadingManager instance = new ThreadingManager();
}
