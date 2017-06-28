package com.wordplat.ikvstockchart.entry;

import android.database.Observable;

/**
 * <p>EntryAdapter，未完成的代码</p>
 * <p>Date: 2017/6/7</p>
 *
 * @author afon
 */

public class EntryAdapter {

    private final AdapterDataObservable observable = new AdapterDataObservable();

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    public int getItemCount() {
        return 0;
    }

    /**
     * Returns true if one or more observers are attached to this adapter.
     *
     * @return true if this adapter has observers
     */
    public final boolean hasObservers() {
        return observable.hasObservers();
    }

    /**
     * Register a new observer to listen for data changes.
     *
     * @param observer Observer to register
     */
    public void registerAdapterDataObserver(AdapterDataObserver observer) {
        observable.registerObserver(observer);
    }

    /**
     * Unregister an observer currently listening for data changes.
     *
     * @param observer Observer to unregister
     *
     * @see #registerAdapterDataObserver
     */
    public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
        observable.unregisterObserver(observer);
    }

    /**
     * Notify any registered observers that the data set has changed.
     *
     * @see #notifyItemChanged(int)
     * @see #notifyItemInserted(int)
     * @see #notifyItemRemoved(int)
     * @see #notifyItemRangeChanged(int, int)
     * @see #notifyItemRangeInserted(int, int)
     * @see #notifyItemRangeRemoved(int, int)
     */
    public final void notifyDataSetChanged() {
        observable.notifyChanged();
    }

    /**
     * Notify any registered observers that the item at <code>position</code> has changed.
     * Equivalent to calling <code>notifyItemChanged(position, null);</code>.
     *
     * @param position Position of the item that has changed
     *
     * @see #notifyItemRangeChanged(int, int)
     */
    public final void notifyItemChanged(int position) {
        observable.notifyItemRangeChanged(position, 1);
    }

    /**
     * Notify any registered observers that the item at <code>position</code> has changed with an
     * optional payload object.
     *
     * @param position Position of the item that has changed
     * @param payload Optional parameter, use null to identify a "full" update
     *
     * @see #notifyItemRangeChanged(int, int)
     */
    public final void notifyItemChanged(int position, Object payload) {
        observable.notifyItemRangeChanged(position, 1, payload);
    }

    /**
     * Notify any registered observers that the <code>itemCount</code> items starting at
     * position <code>positionStart</code> have changed.
     * Equivalent to calling <code>notifyItemRangeChanged(position, itemCount, null);</code>.
     *
     * @param positionStart Position of the first item that has changed
     * @param itemCount Number of items that have changed
     *
     * @see #notifyItemChanged(int)
     */
    public final void notifyItemRangeChanged(int positionStart, int itemCount) {
        observable.notifyItemRangeChanged(positionStart, itemCount);
    }

    /**
     * Notify any registered observers that the <code>itemCount</code> items starting at
     * position <code>positionStart</code> have changed. An optional payload can be
     * passed to each changed item.
     *
     * @param positionStart Position of the first item that has changed
     * @param itemCount Number of items that have changed
     * @param payload  Optional parameter, use null to identify a "full" update
     *
     * @see #notifyItemChanged(int)
     */
    public final void notifyItemRangeChanged(int positionStart, int itemCount, Object payload) {
        observable.notifyItemRangeChanged(positionStart, itemCount, payload);
    }

    /**
     * Notify any registered observers that the item reflected at <code>position</code>
     * has been newly inserted. The item previously at <code>position</code> is now at
     * position <code>position + 1</code>.
     *
     * @param position Position of the newly inserted item in the data set
     *
     * @see #notifyItemRangeInserted(int, int)
     */
    public final void notifyItemInserted(int position) {
        observable.notifyItemRangeInserted(position, 1);
    }

    /**
     * Notify any registered observers that the item reflected at <code>fromPosition</code>
     * has been moved to <code>toPosition</code>.
     *
     * @param fromPosition Previous position of the item.
     * @param toPosition New position of the item.
     */
    public final void notifyItemMoved(int fromPosition, int toPosition) {
        observable.notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * Notify any registered observers that the currently reflected <code>itemCount</code>
     * items starting at <code>positionStart</code> have been newly inserted. The items
     * previously located at <code>positionStart</code> and beyond can now be found starting
     * at position <code>positionStart + itemCount</code>.
     *
     * @param positionStart Position of the first item that was inserted
     * @param itemCount Number of items inserted
     *
     * @see #notifyItemInserted(int)
     */
    public final void notifyItemRangeInserted(int positionStart, int itemCount) {
        observable.notifyItemRangeInserted(positionStart, itemCount);
    }

    /**
     * Notify any registered observers that the item previously located at <code>position</code>
     * has been removed from the data set. The items previously located at and after
     * <code>position</code> may now be found at <code>oldPosition - 1</code>.
     *
     * @param position Position of the item that has now been removed
     *
     * @see #notifyItemRangeRemoved(int, int)
     */
    public final void notifyItemRemoved(int position) {
        observable.notifyItemRangeRemoved(position, 1);
    }

    /**
     * Notify any registered observers that the <code>itemCount</code> items previously
     * located at <code>positionStart</code> have been removed from the data set. The items
     * previously located at and after <code>positionStart + itemCount</code> may now be found
     * at <code>oldPosition - itemCount</code>.
     *
     * @param positionStart Previous position of the first item that was removed
     * @param itemCount Number of items removed from the data set
     */
    public final void notifyItemRangeRemoved(int positionStart, int itemCount) {
        observable.notifyItemRangeRemoved(positionStart, itemCount);
    }

    /**
     * Observer base class for watching changes to an adapter.
     */
    public static abstract class AdapterDataObserver {
        public void onChanged() {
            // Do nothing
        }

        public void onItemRangeChanged(int positionStart, int itemCount) {
            // do nothing
        }

        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            // fallback to onItemRangeChanged(positionStart, itemCount) if app
            // does not override this method.
            onItemRangeChanged(positionStart, itemCount);
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
            // do nothing
        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
            // do nothing
        }

        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            // do nothing
        }
    }

    static class AdapterDataObservable extends Observable<AdapterDataObserver> {
        public boolean hasObservers() {
            return !mObservers.isEmpty();
        }

        public void notifyChanged() {
            // since onChanged() is implemented by the app, it could do anything, including
            // removing itself from {@link mObservers} - and that could cause problems if
            // an iterator is used on the ArrayList {@link mObservers}.
            // to avoid such problems, just march thru the list in the reverse order.
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onChanged();
            }
        }

        public void notifyItemRangeChanged(int positionStart, int itemCount) {
            notifyItemRangeChanged(positionStart, itemCount, null);
        }

        public void notifyItemRangeChanged(int positionStart, int itemCount, Object payload) {
            // since onItemRangeChanged() is implemented by the app, it could do anything, including
            // removing itself from {@link mObservers} - and that could cause problems if
            // an iterator is used on the ArrayList {@link mObservers}.
            // to avoid such problems, just march thru the list in the reverse order.
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onItemRangeChanged(positionStart, itemCount, payload);
            }
        }

        public void notifyItemRangeInserted(int positionStart, int itemCount) {
            // since onItemRangeInserted() is implemented by the app, it could do anything,
            // including removing itself from {@link mObservers} - and that could cause problems if
            // an iterator is used on the ArrayList {@link mObservers}.
            // to avoid such problems, just march thru the list in the reverse order.
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onItemRangeInserted(positionStart, itemCount);
            }
        }

        public void notifyItemRangeRemoved(int positionStart, int itemCount) {
            // since onItemRangeRemoved() is implemented by the app, it could do anything, including
            // removing itself from {@link mObservers} - and that could cause problems if
            // an iterator is used on the ArrayList {@link mObservers}.
            // to avoid such problems, just march thru the list in the reverse order.
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onItemRangeRemoved(positionStart, itemCount);
            }
        }

        public void notifyItemMoved(int fromPosition, int toPosition) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onItemRangeMoved(fromPosition, toPosition, 1);
            }
        }
    }
}
