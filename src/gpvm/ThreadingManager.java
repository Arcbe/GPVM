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
  /**
   * Returns the current instance of the ThreadingManager.
   * 
   * @return The current instance.
   */
  public static ThreadingManager getInstance() {
    return instance;
  }
  
  /**
   * Checks to see whether the write lock is currently locked by the
   * current thread.
   * 
   * @return Whether it is safe for the current thread to write to the model.
   */
  public boolean canWrite() {
    return lock.isWriteLockedByCurrentThread();
  }
  
  /**
   * Checks to see whether the read lock is locked by the current thread.
   * 
   * @return Whether it is safe for the current thread to read from the model.
   */
  public boolean canRead() {
    return lock.getReadHoldCount() > 0;
  }
  
  /**
   * Requests that the read lock be locked for the current thread.  This
   * method may not return instantly if the write lock is currently locked.
   */
  public void requestRead() {
    lock.readLock().lock();
  }
  
  /**
   * Releases the read lock for the current thread.
   */
  public void releaseRead() {
    lock.readLock().unlock();
  }
  
  /**
   * Requests that the write lock be locked for the current thread. This
   * method may not return instantly if the read lock is locked by any threads.
   * Any thread that has a read lock should not try to lock the write lock.
   */
  public void requestWrite() {
    lock.writeLock().lock();
  }
  
  /**
   * Releases the write lock for the current thread.
   */
  public void releaseWrite() {
    lock.writeLock().unlock();
  }
  
  private ThreadingManager() {
    lock = new ReentrantReadWriteLock();
  }
  
  private ReentrantReadWriteLock lock;
  
  private static ThreadingManager instance = new ThreadingManager();
}
