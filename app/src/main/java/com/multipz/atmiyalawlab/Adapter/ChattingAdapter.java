package com.multipz.atmiyalawlab.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.multipz.atmiyalawlab.Model.ChatMessage;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.multipz.atmiyalawlab.Util.Shared;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Admin on 30-10-2017.
 */

public class ChattingAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private Context mContext;
    private List<ChatMessage> mMessageList;
    private ItemClickListener clickListener;

    private int mediaFileLengthInMilliseconds;
    final MediaPlayer mediaPlayer = new MediaPlayer();
    private Shared shared;


    public ChattingAdapter(Context context, List<ChatMessage> messageList) {
        mContext = context;
        mMessageList = messageList;
        shared = new Shared(mContext);
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = (ChatMessage) mMessageList.get(position);
        if (message.getUserId().equals(/*"28"*/shared.getUserId())) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.right, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.left, parent, false);
            return new ReceivedMessageHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = (ChatMessage) mMessageList.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
                break;
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView msgright, timeText;
        private ImageView imagesenduserright, downloadfileright, displayaudioright, imgplaymusicright;
        private SeekBar seektune_customright;
        private LinearLayout layoutaudioright, layoutDocFileDownloadright;

        SentMessageHolder(View itemView) {
            super(itemView);
            msgright = (TextView) itemView.findViewById(R.id.msgright);
            imagesenduserright = (ImageView) itemView.findViewById(R.id.imagesenduserright);
            downloadfileright = (ImageView) itemView.findViewById(R.id.downloadfileright);
            displayaudioright = (ImageView) itemView.findViewById(R.id.displayaudioright);
            imgplaymusicright = (ImageView) itemView.findViewById(R.id.imgplaymusicright);
            seektune_customright = (SeekBar) itemView.findViewById(R.id.seektune_customright);
            layoutaudioright = (LinearLayout) itemView.findViewById(R.id.layoutaudioright);
            layoutDocFileDownloadright = (LinearLayout) itemView.findViewById(R.id.layoutDocFileDownloadright);
            layoutDocFileDownloadright.setOnClickListener(this);
            imagesenduserright.setOnClickListener(this);
        }

        void bind(ChatMessage message) {
            String type = message.getMsgType();
            if (type.matches("Msg")) {
                msgright.setVisibility(View.VISIBLE);
                msgright.setText(message.getMessage());
                imagesenduserright.setVisibility(View.GONE);
                layoutDocFileDownloadright.setVisibility(View.GONE);
            } else if (type.matches("Media")) {
                msgright.setVisibility(View.GONE);
                if (message.getSendMsgType() != null) {
                    if (message.getSendMsgType().matches("image")) {
                        imagesenduserright.setVisibility(View.VISIBLE);
                        Glide.with(mContext).load(message.getMessage())
                                .crossFade()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imagesenduserright);
                    } else if (message.getSendMsgType().matches("audio")) {
                        layoutaudioright.setVisibility(View.VISIBLE);
                    } else if (message.getSendMsgType().matches("primary")) {
                        layoutDocFileDownloadright.setVisibility(View.VISIBLE);
                    } else {
                        imagesenduserright.setVisibility(View.VISIBLE);
                        Bitmap bitmap = getCompressedBitmap(message.getMessage());
                        imagesenduserright.setImageBitmap(bitmap);
                    }
                }
            }
            itemView.setOnClickListener(this);
            layoutaudioright.setOnClickListener(this);
            downloadfileright.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition());
            }
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView messageText, audiotime;
        private ImageView imagesenduserleft, downloadfile, imgplaymusic, imgpausemusic;
        private SeekBar seektune;
        private LinearLayout layoutaudio, layoutDocFileDownloadleft;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.msgr);
            audiotime = (TextView) itemView.findViewById(R.id.audiotime);
            imagesenduserleft = (ImageView) itemView.findViewById(R.id.imagesenduserleft);
            downloadfile = (ImageView) itemView.findViewById(R.id.downloadfile);
            imgplaymusic = (ImageView) itemView.findViewById(R.id.imgplaymusic);
            imgpausemusic = (ImageView) itemView.findViewById(R.id.imgpausemusic);

            seektune = (SeekBar) itemView.findViewById(R.id.seektune_custom);
            layoutaudio = (LinearLayout) itemView.findViewById(R.id.layoutaudio);
            layoutDocFileDownloadleft = (LinearLayout) itemView.findViewById(R.id.layoutDocFileDownloadleft);
            imagesenduserleft.setOnClickListener(this);
        }

        void bind(ChatMessage message) {
            String type = message.getMsgType();
            if (type.matches("Msg")) {
                messageText.setVisibility(View.VISIBLE);
                imagesenduserleft.setVisibility(View.GONE);
                layoutDocFileDownloadleft.setVisibility(View.GONE);
                messageText.setText(message.getMessage());
            } else if (type.matches("Media")) {
                messageText.setVisibility(View.GONE);
                if (message.getSendMsgType() != null) {
                    if (message.getSendMsgType().matches("image")) {
                        imagesenduserleft.setVisibility(View.VISIBLE);
                        Glide.with(mContext).load(message.getMessage())
                                .crossFade()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imagesenduserleft);
                    } else if (message.getSendMsgType().matches("audio")) {
                        layoutaudio.setVisibility(View.VISIBLE);
                    } else if (message.getSendMsgType().matches("primary")) {
                        layoutDocFileDownloadleft.setVisibility(View.VISIBLE);
                    } else {

                    }
                }
                itemView.setOnClickListener(this);
                downloadfile.setOnClickListener(this);
                layoutaudio.setOnClickListener(this);

            }
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition());
            }
        }
    }

    public Bitmap getCompressedBitmap(String imagePath) {
        float maxHeight = 1920.0f;
        float maxWidth = 1080.0f;
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(imagePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);

        byte[] byteArray = out.toByteArray();

        Bitmap updatedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        return updatedBitmap;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }
}
