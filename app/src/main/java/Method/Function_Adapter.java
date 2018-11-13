package Method;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.louis.theclass.R;
import java.util.List;
public class Function_Adapter extends RecyclerView.Adapter<Function_Adapter.MyViewHolder> {
    private List<Integer> item_icon_left;
    private List<String> item_text;
    private LayoutInflater inflater;
    private Function_Adapter.ItemClickListener ClickListener;
    private Activity activity;
    public Function_Adapter(Activity activity, Context context, List<Integer> item_icon_left, List<String> item_text) {
        this.item_icon_left = item_icon_left;
        this.item_text = item_text;
        this.activity=activity;
        inflater = LayoutInflater.from(context);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView icon_left;
        private TextView text;
        private CheckBox checkBox;
        private CardView c1;
        public MyViewHolder(View itemView){
            super(itemView);
            icon_left=(ImageView) itemView.findViewById(R.id.functionpage_img_left);
            text=(TextView)itemView.findViewById(R.id.functionpage_text);
            c1=(CardView) itemView.findViewById(R.id.functionpage_ca);
            checkBox=(CheckBox)itemView.findViewById(R.id.functionpage_checkbox);
        }
    }

    @Override
    public Function_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Function_Adapter.MyViewHolder holder = new Function_Adapter.MyViewHolder(inflater.inflate(R.layout.function_rv_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final Function_Adapter.MyViewHolder holder, final int position) {
        holder.icon_left.setBackgroundResource(item_icon_left.get(position));
        holder.text.setText(item_text.get(position));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    holder.checkBox.setText("已完成");
                    holder.checkBox.setTextColor(Color.GREEN);
                }else{
                    holder.checkBox.setText("未完成");
                    holder.checkBox.setTextColor(Color.RED);
                }
            }
        });
        holder.c1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ClickListener.OnItemClick(view,position);
            }
        });
    }
    public Function_Adapter setClickListener(Function_Adapter.ItemClickListener ClickListener){
        this.ClickListener = ClickListener;
        return this;
    }
    @Override
    public int getItemCount() {
        return item_text.size();
    }
    public interface ItemClickListener{
        //声明接口ItemClickListener
        void OnItemClick(View view,int position);
//        void OnTitleClick(View view,int position);
//        void OnInfoClick(View view,int position);
//        void OnTypeClick(View view,int position);
    }

}
