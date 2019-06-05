package address.teruten.com.terutenaddress.ui.cafeteria;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import address.teruten.com.terutenaddress.R;
import address.teruten.com.terutenaddress.present.cafeteria.CafeteriaPresent;
import address.teruten.com.terutenaddress.present.cafeteria.CafeteriaPresentImpl;
import address.teruten.com.terutenaddress.ui.cafeteria.adapter.CafeteriaAdapter;
import address.teruten.com.terutenaddress.vo.CafeteriaMenu;
import address.teruten.com.terutenaddress.vo.network.Cafeteria;

public class CafeteriaActivity extends AppCompatActivity implements CafeteriaPresent.View {

    private RecyclerView recyclerView;
    private CafeteriaAdapter cafeteriaAdapter;
    private CafeteriaPresentImpl cafeteriaPresentImpl;

    private AVLoadingIndicatorView avi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafeteria);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("구내식당 메뉴");
        setSupportActionBar(toolbar);
        avi= (AVLoadingIndicatorView) findViewById(R.id.cafeteria_indicator);
        cafeteriaPresentImpl = new CafeteriaPresentImpl(this, this);

        recyclerView = (RecyclerView) findViewById(R.id.cafeterial_recycler_view);
        cafeteriaAdapter = new CafeteriaAdapter();
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cafeteriaAdapter);

        cafeteriaPresentImpl.loadMenu();

    }

    @Override
    public void setMenu(ArrayList<CafeteriaMenu> menus) {
        cafeteriaAdapter.setList(menus);
        cafeteriaAdapter.notifyDataSetChanged();
    }

    @Override
    public void hideIndicator() {
        avi.hide();
    }

    @Override
    public void showIndicator() {
        avi.show();
    }
}
