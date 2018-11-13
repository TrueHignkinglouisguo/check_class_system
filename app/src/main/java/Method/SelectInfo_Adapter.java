package Method;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import com.example.louis.theclass.R;
import java.util.List;
public class SelectInfo_Adapter extends RecyclerView.Adapter<SelectInfo_Adapter.MyViewHolder> {
    private List<String> item_text;
    private LayoutInflater inflater;
    private SelectInfo_Adapter.ItemClickListener ClickListener;
    private Activity activity;
    public SelectInfo_Adapter(Activity activity, Context context, List<String> item_text) {
        this.item_text = item_text;
        this.activity=activity;
        inflater = LayoutInflater.from(context);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView text;
        private TableRow tb1;
        public MyViewHolder(View itemView){
            super(itemView);
            text=(TextView)itemView.findViewById(R.id.selectinfo_rv_text);
            tb1=(TableRow) itemView.findViewById(R.id.selectinfo_rv_tb);
        }
    }

    @Override
    public SelectInfo_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SelectInfo_Adapter.MyViewHolder holder = new SelectInfo_Adapter.MyViewHolder(inflater.inflate(R.layout.selectinfo_item, parent, false));
        return holder;
    }

    public void onBindViewHolder(final SelectInfo_Adapter.MyViewHolder holder, final int position) {
        holder.text.setText(item_text.get(position));
        holder.tb1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ClickListener.OnItemClick(view,position);
            }
        });
    }
    public SelectInfo_Adapter setClickListener(SelectInfo_Adapter.ItemClickListener ClickListener){
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
