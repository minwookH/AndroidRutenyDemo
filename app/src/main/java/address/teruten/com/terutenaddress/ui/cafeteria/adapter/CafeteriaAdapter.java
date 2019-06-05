package address.teruten.com.terutenaddress.ui.cafeteria.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import address.teruten.com.terutenaddress.R;
import address.teruten.com.terutenaddress.vo.CafeteriaMenu;
import address.teruten.com.terutenaddress.vo.network.Cafeteria;

/**
 * Created by teruten on 2017-08-10.
 */

public class CafeteriaAdapter extends RecyclerView.Adapter<CafeteriaAdapter.CafeteriaViewHolder>{

    private ArrayList<CafeteriaMenu> cafeteriaMenus = new ArrayList<>();

    @Override
    public CafeteriaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cafeteria, parent, false);
        //view.setOnClickListener(this);
        return new CafeteriaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CafeteriaViewHolder holder, int position) {
        holder.menuDate.setText(cafeteriaMenus.get(position).getDate());

        ArrayList<String> lunch = cafeteriaMenus.get(position).getLunch();
        StringBuilder stringBuilder = new StringBuilder();
        for (String menu:lunch) {
            stringBuilder.append(menu+"\n");
        }

        holder.lunchMenu.setText(stringBuilder.toString());

        ArrayList<String> dinner = cafeteriaMenus.get(position).getDinner();
        stringBuilder = new StringBuilder();
        for (String menu:dinner) {
            stringBuilder.append(menu+"\n");
        }

        holder.dinnerMenu.setText(stringBuilder.toString());
    }

    @Override
    public int getItemCount() {
        return cafeteriaMenus.size();
    }

    public void setList(ArrayList<CafeteriaMenu> cafeteriaMenus){
        this.cafeteriaMenus.addAll(cafeteriaMenus);
    }

    class CafeteriaViewHolder extends RecyclerView.ViewHolder{

        private TextView menuDate;
        private TextView lunchMenu;
        private TextView dinnerMenu;

        public CafeteriaViewHolder(View itemView) {
            super(itemView);
            menuDate = (TextView) itemView.findViewById(R.id.cafeteria_date);
            lunchMenu = (TextView) itemView.findViewById(R.id.cafeteria_launch);
            dinnerMenu = (TextView) itemView.findViewById(R.id.cafeteria_dinner);
        }
    }
}
