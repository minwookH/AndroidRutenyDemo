package address.teruten.com.terutenaddress.ui.setting;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import address.teruten.com.terutenaddress.R;
import address.teruten.com.terutenaddress.vo.MailSelectedMember;
import address.teruten.com.terutenaddress.vo.Member;

/**
 * Created by teruten on 2017-07-17.
 */

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.MemberListViewHolder> implements View.OnClickListener {
    private ArrayList<MailSelectedMember> memberList = new ArrayList<>();
    private ArrayList<Member> checkedList = new ArrayList<>();

    @Override
    public MemberListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_member_list, parent, false);
        view.setOnClickListener(this);
        return new MemberListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MemberListViewHolder holder, final int position) {
        holder.memberName.setText(memberList.get(position).getMember().getName()+" "+memberList.get(position).getMember().getPosition());
        holder.isSelected.setChecked(memberList.get(position).isSelected());
        holder.isSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean flag) {
                Log.d("MemberListViewHolder", "isSelected flag : "+flag);
                Log.d("MemberListViewHolder", "isSelected name : "+memberList.get(holder.getAdapterPosition()).getMember().getName());
                if(holder.getAdapterPosition()!=RecyclerView.NO_POSITION){
                    memberList.get(holder.getAdapterPosition()).setSelected(flag);
                }
            }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.isSelected.isChecked()){
                    holder.isSelected.setChecked(false);
                }else{
                    holder.isSelected.setChecked(true);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    @Override
    public void onClick(View view) {

    }

    class MemberListViewHolder extends RecyclerView.ViewHolder{
        private TextView memberName;
        private CheckBox isSelected;
        private View view;

        MemberListViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            memberName = (TextView) itemView.findViewById(R.id.item_member_list_text);
            isSelected = (CheckBox) itemView.findViewById(R.id.item_member_list_check_box);
        }
    }

    void addMemberList(ArrayList<MailSelectedMember> list){
        this.memberList.addAll(list);
    }

    public ArrayList<MailSelectedMember> getCheckedList(){
        return memberList;
    }

}
