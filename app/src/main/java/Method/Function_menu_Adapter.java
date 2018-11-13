package Method;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.louis.theclass.R;
import java.util.List;
public class Function_menu_Adapter extends RecyclerView.Adapter<Function_menu_Adapter.MyViewHolder> {
    private List<Integer> item_icon;
    private List<String> item_text;
    private LayoutInflater inflater;
    private Function_menu_Adapter.ItemClickListener ClickListener;
    private Activity activity;
    public Function_menu_Adapter(Activity activity, Context context, List<Integer> item_icon, List<String> item_text) {
        this.item_icon = item_icon;
        this.item_text = item_text;
        this.activity=activity;
        inflater = LayoutInflater.from(context);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView icon;
        private TextView text;
        public MyViewHolder(View itemView){
            super(itemView);
            icon=(ImageView) itemView.findViewById(R.id.functionpage_menu_pic);
            text=(TextView)itemView.findViewById(R.id.functionpage_menu_text);
        }
    }

    @Override
    public Function_menu_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Function_menu_Adapter.MyViewHolder holder = new Function_menu_Adapter.MyViewHolder(inflater.inflate(R.layout.functionpage_menu, parent, false));
        return holder;
    }
    public void onBindViewHolder(final Function_menu_Adapter.MyViewHolder holder, final int position) {
        holder.icon.setBackgroundResource(item_icon.get(position));
        holder.text.setText(item_text.get(position));
        holder.icon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ClickListener.OnItemClick(view,position);
            }
        });
    }
    public Function_menu_Adapter setClickListener(Function_menu_Adapter.ItemClickListener ClickListener){
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
