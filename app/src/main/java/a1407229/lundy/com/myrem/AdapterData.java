package a1407229.lundy.com.myrem;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class AdapterData  extends RecyclerView.Adapter<ViewHolderData>  {

    ArrayList<Data> alData;

    public AdapterData(ArrayList<Data> alData) {
        this.alData = alData;
    }

    @NonNull
    @Override
    public ViewHolderData onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row, viewGroup, false);
        return new ViewHolderData(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderData viewHolderData, int i) {
        Data d = alData.get(i);
        viewHolderData.tv1.setText(d.getData1());
        viewHolderData.tv2.setText(d.getData2());
		
		//diperbaiki
		//viewHolderData.NAMA_BUTTON.setOnClickListener

    }

    @Override
    public int getItemCount() {
        return alData.size();
    }
}
