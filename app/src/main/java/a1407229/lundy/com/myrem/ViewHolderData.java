package a1407229.lundy.com.myrem;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ViewHolderData  extends RecyclerView.ViewHolder {
    public TextView tv1,tv2;

    public ViewHolderData (View itemView) {
        super(itemView);
        tv1 = itemView.findViewById(R.id.tv1);
        tv2 = itemView.findViewById(R.id.tv2);
    }

}
