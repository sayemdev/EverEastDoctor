package evereast.co.doctor.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import evereast.co.doctor.Activity.ViewImageActivity;
import evereast.co.doctor.R;
import evereast.co.doctor.utils.GetTimeAgo;

import static evereast.co.doctor.Constants.MAIN_URL;
import static evereast.co.doctor.Constants.PROFILE_FILES;
import static evereast.co.doctor.Constants.USER_DATA_PREF;
import static evereast.co.doctor.Constants.USER_ID;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "SOMETAG";
    private static final int TYPE_NULL = 3;
    private static final int TYPE_SEND = 1;
    private static final int TYPE_RECEIVE = 2;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    List<Messages> messagesList;
    ChatActivity context;
    String currentUserId;

    public ChatAdapter(List<Messages> messagesList, ChatActivity context) {
        this.messagesList = messagesList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SEND) {
            View view = LayoutInflater.from(context).inflate(R.layout.send1, parent, false);
            return new SendMessageViewHolder(view);
        } else if (viewType == TYPE_RECEIVE) {
            return new ReceivedMessageViewHolder(LayoutInflater.from(context).inflate(R.layout.recieve1, parent, false));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Messages messages = messagesList.get(position);

        SendMessageViewHolder sendMessageViewHolder = new SendMessageViewHolder(holder.itemView);
        ReceivedMessageViewHolder receivedMessageViewHolder = new ReceivedMessageViewHolder(holder.itemView);

        try {
            Glide.with(context).load(PROFILE_FILES + context.chatUser + ".jpg").placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(receivedMessageViewHolder.circularImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }


        sendMessageViewHolder.setMessage(messages, position, messages.getCurrentUserId());
        receivedMessageViewHolder.setReceiveMsg(messages, position, messages.getChatUserId());
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        currentUserId = context.getSharedPreferences(USER_DATA_PREF, Context.MODE_PRIVATE).getString(USER_ID, "Anonymous");
//        currentUserId = "Saimon";
        if (messagesList.get(position).getFrom().equals(currentUserId)) {
            return TYPE_SEND;
        } else if (messagesList.size() == 0) {
            return TYPE_NULL;
        } else {
            return TYPE_RECEIVE;
        }
    }

    private class SendMessageViewHolder extends RecyclerView.ViewHolder {

        private final ImageView sendImgView;
        TextView sendMessageTextView, sendSeenTextView, sendTimeTextView;
        String currentState = "unClicked";
        CircularImageView imageView;

        public SendMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            sendMessageTextView = itemView.findViewById(R.id.txtName);
            sendTimeTextView = itemView.findViewById(R.id.txtAddress);
            sendSeenTextView = itemView.findViewById(R.id.txtSeen);
            imageView = itemView.findViewById(R.id.profileImage);
            sendImgView = itemView.findViewById(R.id.sendImgV);
        }

        private void setMessage(Messages messages, int position, String currentUserId) {
            try {
                if (messages.getType().equals("text")) {
                    sendImgView.setVisibility(View.GONE);
                    sendMessageTextView.setVisibility(View.VISIBLE);
                    sendMessageTextView.setText(messages.getMessage());
                } else {
                    sendImgView.setVisibility(View.VISIBLE);
                    sendMessageTextView.setVisibility(View.GONE);
                    Glide.with(context).load(MAIN_URL + "Chat_Image/" + messages.getMessage()).placeholder(R.drawable.round_health)
                            .error(R.drawable.round_health)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            }).into(sendImgView);

                    sendImgView.setOnClickListener(v -> {
                        Intent intent = new Intent(context, ViewImageActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("link", MAIN_URL + "Chat_Image/" + messages.getMessage());
                        context.startActivity(intent);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String time = GetTimeAgo.getTimeAgo1(Long.parseLong(messages.getTime()), context);
            sendTimeTextView.setText(time);
            String seen = messages.getSeen();

            Log.d(TAG, "setMessage: " + PROFILE_FILES + currentUserId + ".jpg");
            sendMessageTextView.setOnClickListener(v -> {
                if (currentState.equals("unClicked")) {
                    sendTimeTextView.setVisibility(View.VISIBLE);
                    currentState = "clicked";
                } else if (currentState.equals("clicked")) {
                    sendTimeTextView.setVisibility(View.GONE);
                    currentState = "unClicked";
                }
            });
        }

    }

    private class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView sendImgView;
        TextView messageTextView, timeTextView;
        String currentState = "unClicked";
        CircularImageView circularImageView;

        public ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.txtName);
            timeTextView = itemView.findViewById(R.id.txtAddress);
            circularImageView = itemView.findViewById(R.id.chatUserProf);
            sendImgView = itemView.findViewById(R.id.sendImgV);
        }

        private void setReceiveMsg(final Messages messages, int position, String chatUserId) {
//            messageTextView.setText(messages.getMessage());
            try {
                if (messages.getType().equals("text")) {
                    sendImgView.setVisibility(View.GONE);
                    messageTextView.setVisibility(View.VISIBLE);
                    messageTextView.setText(messages.getMessage());
                } else {
                    sendImgView.setVisibility(View.VISIBLE);
                    messageTextView.setVisibility(View.GONE);
                    Glide.with(context).load(MAIN_URL + "Chat_Image/" + messages.getMessage()).placeholder(R.drawable.round_health)
                            .error(R.drawable.round_health)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            }).into(sendImgView);
                    sendImgView.setOnClickListener(v -> {
                        Intent intent = new Intent(context, ViewImageActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("link", MAIN_URL + "Chat_Image/" + messages.getMessage());
                        context.startActivity(intent);
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String time = GetTimeAgo.getTimeAgo1(Long.parseLong(messages.getTime()), context);
            timeTextView.setText(time);
            Log.d(TAG, "setReceiveMsg: " + PROFILE_FILES + chatUserId + ".jpg");
            messageTextView.setOnClickListener(v -> {
                if (currentState.equals("unClicked")) {
                    timeTextView.setVisibility(View.VISIBLE);
                    currentState = "clicked";
                } else if (currentState.equals("clicked")) {
                    timeTextView.setVisibility(View.GONE);
                    currentState = "unClicked";
                }
            });
        }
    }

    private class NullMessageViewHolder extends RecyclerView.ViewHolder {
        TextView welcomingTextView;

        public NullMessageViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
