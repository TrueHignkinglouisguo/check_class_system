package Method;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import com.example.louis.theclass.R;
import java.util.List;
public class Me_Adapter extends RecyclerView.Adapter<Me_Adapter.MyViewHolder> {
    private List<Integer> item_icon_left;
    private List<String> item_text;
    private LayoutInflater inflater;
    private ItemClickListener ClickListener;
    private Activity activity;
    public Me_Adapter(Activity activity, Context context, List<Integer> item_icon_left, List<String> item_text) {
        this.item_icon_left = item_icon_left;
        this.item_text = item_text;
        this.activity=activity;
        inflater = LayoutInflater.from(context);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView icon_left;
        private TextView text;
        private TableRow tb1;
        public MyViewHolder(View itemView){
            super(itemView);
            icon_left=(ImageView) itemView.findViewById(R.id.mepage_rv_icon_left);
            text=(TextView)itemView.findViewById(R.id.mepage_rv_text);
            tb1=(TableRow)itemView.findViewById(R.id.mepage_rv_tb1);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(inflater.inflate(R.layout.mepage_rv_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position) {
        holder.icon_left.setBackgroundResource(item_icon_left.get(position));
        holder.text.setText(item_text.get(position));
        holder.tb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickListener.OnItemClick(view,position);
            }
        });

    }
    public Me_Adapter setClickListener(ItemClickListener ClickListener){
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
