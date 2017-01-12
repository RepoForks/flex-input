package com.lytefast.flexinput.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lytefast.flexinput.R;
import com.lytefast.flexinput.R2;
import com.lytefast.flexinput.emoji.Emoji;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Displays an assortment of Emojis for input.
 *
 * @author Sam Shih
 */
public class EmojiGridFragment extends Fragment {

  public static final String EMOJI_CATEGORY = "emoji_category";

  @BindView(R2.id.list) RecyclerView emojiGrid;
  private Unbinder unbinder;

  private Emoji.EmojiCategory emojiCategory;


  @Nullable
  @Override
  public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
                           @Nullable final Bundle savedInstanceState) {

    View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
    unbinder = ButterKnife.bind(this, rootView);

    final int numFittedColumns = calculateNumOfColumns(getResources());
    emojiGrid.setLayoutManager(new GridLayoutManager(getContext(), numFittedColumns));
    emojiGrid.setAdapter(new Adapter());

    if (savedInstanceState != null) {
      emojiCategory = savedInstanceState.getParcelable(EMOJI_CATEGORY);
    }

    return rootView;
  }

  @Override
  public void onDestroyView() {
    unbinder.unbind();
    super.onDestroyView();
  }

  @Override
  public void onSaveInstanceState(final Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelable(EMOJI_CATEGORY, emojiCategory);
  }

  public EmojiGridFragment with(final Emoji.EmojiCategory emojiCategory) {
    this.emojiCategory = emojiCategory;
    return this;
  }

  private class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
      View view = LayoutInflater.from(
          parent.getContext()).inflate(R.layout.view_emoji_item, parent, false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
      Emoji emoji = emojiCategory.emojis.get(position);
      holder.bind(emoji);
    }

    @Override
    public int getItemCount() {
      return emojiCategory.emojis.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
      private final TextView textView;

      public ViewHolder(final View itemView) {
        super(itemView);
        textView = (TextView) itemView;
      }

      public void bind(final Emoji emoji) {
        textView.setText(emoji.strValue);
        textView.setContentDescription(emoji.aliases[0]);

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
          @Override
          public boolean onLongClick(final View v) {
            Toast.makeText(getContext(), emoji.aliases[0], Toast.LENGTH_SHORT).show();
            return true;
          }
        });
      }
    }
  }

  public static int calculateNumOfColumns(Resources resources) {
    DisplayMetrics displayMetrics = resources.getDisplayMetrics();
    int numCols =
        (int) (displayMetrics.widthPixels / resources.getDimension(R.dimen.emoji_grid_item_size));
    return numCols;
  }
}