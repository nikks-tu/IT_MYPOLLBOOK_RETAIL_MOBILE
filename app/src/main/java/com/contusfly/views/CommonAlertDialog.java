/**
 * @category ContusMessanger
 * @package com.contusfly.views
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.views;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

/**
 * The Class CommonAlertDialog.
 */
public class CommonAlertDialog {

    /** The listener. */
    private CommonDialogClosedListener listener;

    /** The context. */
    private Context context;

    /**
     * The Enum DIALOG_TYPE.
     */
    public enum DIALOG_TYPE {

        /** The dialog single. */
        DIALOG_SINGLE,

        /** The dialog dual. */
        DIALOG_DUAL
    }

    /**
     * The listener interface for receiving commonDialogClosed events. The class
     * that is interested in processing a commonDialogClosed event implements
     * this interface, and the object created with that class is registered with
     * a component using the component's
     * <code>addCommonDialogClosedListener<code> method. When
     * the commonDialogClosed event occurs, that object's appropriate
     * method is invoked.
     *
     * @see CommonDialogClosedEvent
     */
    public interface CommonDialogClosedListener {

        /**
         * On dialog closed.
         *
         * @param dialogType
         *            the dialog type
         * @param isSuccess
         *            the is success
         */
        void onDialogClosed(DIALOG_TYPE dialogType, boolean isSuccess);

        /**
         * List option selected.
         *
         * @param position
         *            the position
         */
        void listOptionSelected(int position);
    }

    /**
     * Instantiates a new common alert dialog.
     *
     * @param context
     *            the context
     */
    public CommonAlertDialog(Context context) {
        this.context = context;
    }

    /**
     * Sets the on dialog close listener.
     *
     * @param listener
     *            the new on dialog close listener
     */
    public void setOnDialogCloseListener(CommonDialogClosedListener listener) {
        this.listener = listener;
    }

    /**
     * Show alert dialog.
     *
     * @param title
     *            the title
     * @param msg
     *            the msg
     * @param positiveString
     *            the positive string
     * @param negativeString
     *            the negative string
     * @param dialogType
     *            the dialog type
     */
    public void showAlertDialog(String title, String msg,
            String positiveString, String negativeString,
            final DIALOG_TYPE dialogType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        if (!title.isEmpty())
            builder.setTitle(title);
        if (dialogType.equals(DIALOG_TYPE.DIALOG_DUAL)) {
            builder.setNegativeButton(negativeString,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (listener != null)
                                listener.onDialogClosed(dialogType, false);
                        }
                    });
        }
        builder.setPositiveButton(positiveString,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (listener != null)
                            listener.onDialogClosed(dialogType, true);
                    }
                });
        builder.create().show();
    }

    /**
     * Show list dialog.
     *
     * @param title
     *            the title
     * @param listItems
     *            the list items
     */
    public void showListDialog(String title, String[] listItems) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!title.isEmpty())
            builder.setTitle(title);
        builder.setItems(listItems, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                dialog.dismiss();
                if (listener != null)
                    listener.listOptionSelected(item);
            }
        });
        builder.create().show();
    }

}
