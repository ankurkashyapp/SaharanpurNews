package kashapps.news.saharanpur.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.malinskiy.superrecyclerview.swipe.BaseSwipeAdapter;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import kashapps.news.saharanpur.R;
import kashapps.news.saharanpur.api.responses.JokesResponse;

/**
 * Created by ankur on 2/8/17.
 */

public class JokesListAdapter extends BaseSwipeAdapter<BaseSwipeAdapter.BaseSwipeableViewHolder> {
    private Context context;
    private List<JokesResponse> jokesResponses;
    private JokeItemClickListener jokeItemClickListener;

    public JokesListAdapter(Context context, List<JokesResponse> jokesResponses, JokeItemClickListener jokeItemClickListener) {
        this.context = context;
        this.jokesResponses = jokesResponses;
        this.jokeItemClickListener = jokeItemClickListener;
    }

    public void setJokesList(List<JokesResponse> jokesResponses) {
        this.jokesResponses = jokesResponses;
    }

    @Override
    public BaseSwipeableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_joke, parent, false);
        return new JokeItemViewHolder(view, jokeItemClickListener);
    }

    @Override
    public void onBindViewHolder(BaseSwipeableViewHolder holder, int position, List<Object> payloads) {
        JokesResponse jokesResponse = jokesResponses.get(position);
        JokeItemViewHolder jokeItemViewHolder = (JokeItemViewHolder)holder;
        Picasso.with(context).load(jokesResponse.getImage()).into(jokeItemViewHolder.itemImage);
        jokeItemViewHolder.itemTitle.setText(jokesResponse.getTitle());
    }

    @Override
    public int getItemCount() {
        return jokesResponses.size();
    }

    private class JokeItemViewHolder extends BaseSwipeableViewHolder {
        public ImageView itemImage;
        public TextView itemTitle;

        public JokeItemViewHolder(final View itemView, final JokeItemClickListener jokeItemClickListener) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.item_image_main);
            itemTitle = (TextView)itemView.findViewById(R.id.item_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    jokeItemClickListener.onJokeClick(itemView);
                }
            });
        }
    }

    public interface JokeItemClickListener {
        void onJokeClick(View itemView);
    }
}
