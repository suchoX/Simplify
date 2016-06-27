package com.ns3.simplify;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class Homescreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.main_back));
        }
    }

    public void bluetoothClicked(View v){
        Intent intent = new Intent(this, Attendance.class);
        startActivity(intent);
    }

    public void wifiClicked(View v){
        Intent intent = new Intent(this, WifiLandingPage.class);
        startActivity(intent);
    }
}
/*
package com.ns3.simplify;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Canvas;
        import android.graphics.Paint;
        import android.graphics.Rect;
        import android.graphics.RectF;
        import android.graphics.Typeface;
        import android.os.Build;
        import android.os.Bundle;
        import android.support.v4.content.ContextCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.view.MotionEvent;
        import android.view.SurfaceView;
        import android.view.View;
        import android.view.Window;
        import android.view.WindowManager;


public class Homescreen extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        View homescreen_canvas =new Homescreen_canvas(this);
        setContentView(homescreen_canvas);
        if(getIntent().getBooleanExtra("Open Bluetooth",false))
        {
            Intent intent = new Intent(Homescreen.this, Attendance.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("Open Bluetooth",true);
            startActivity(intent);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M) {
                window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.white));
                homescreen_canvas.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(getResources().getColor(R.color.card_shadow));
            }
        }
    }

    public class Homescreen_canvas extends SurfaceView
    {
        private int screen_height;
        private int screen_width;
        private Bitmap button_bluetooth;
        private Bitmap button_wifi;
        private BitmapFactory.Options options;
        Paint paint;
        Paint text;
        RectF rectF;
        Rect txt_bounds;
        Typeface type;
        String temp_text;
        Intent intent;
        public Homescreen_canvas(Context context)
        {
            super(context);
            if(Build.VERSION.SDK_INT >=23)
                setBackgroundColor(ContextCompat.getColor(context, R.color.main_white));
            else
                setBackgroundColor(getResources().getColor(R.color.main_white));
            options=new BitmapFactory.Options();
            paint =new Paint();
            text=new Paint();
            rectF=new RectF();
            txt_bounds=new Rect();

            paint.setAntiAlias(true);
            button_bluetooth= BitmapFactory.decodeResource(getResources(), R.drawable.home_bluetooth_button, options);
            button_wifi= BitmapFactory.decodeResource(getResources(), R.drawable.home_wifi_button, options);

            type = Typeface.createFromAsset(context.getAssets(),"fonts/app_font.ttf");

            text.setTypeface(type);
            text.setAntiAlias(true);
            text.setColor(getResources().getColor(R.color.grey_text));
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            if(Build.VERSION.SDK_INT >=23)
                paint.setColor(ContextCompat.getColor(getContext(), R.color.main_white));
            else
                paint.setColor(getResources().getColor(R.color.main_white));
            canvas.drawRect(0, screen_height / 2, screen_width, screen_height, paint);    //Drawing the Bottom Pink window
            rectF.set(screen_width/2-button_bluetooth.getWidth()/2,screen_height/4-button_bluetooth.getHeight()/2,screen_width/2+button_bluetooth.getWidth()/2,screen_height/4+button_bluetooth.getHeight()/2);
            canvas.drawBitmap(button_bluetooth,null,rectF,paint);   //Drawing the Top Bluetooth Button
            rectF.set(screen_width/2-button_wifi.getWidth()/2,(3*screen_height)/4-button_wifi.getHeight()/2,screen_width/2+button_wifi.getWidth()/2,(3*screen_height)/4+button_bluetooth.getHeight()/2);
            canvas.drawBitmap(button_wifi,null,rectF,paint);   //Drawing the Top Bluetooth Button
            type_text(canvas);
            invalidate();
        }

        private void type_text(Canvas canvas)
        {
            temp_text="Bluetooth Attendance ";
            text.getTextBounds(temp_text,0,temp_text.length(),txt_bounds);
            canvas.drawText(temp_text, screen_width/2 - txt_bounds.exactCenterX(), screen_height/4+button_bluetooth.getHeight()/2+screen_height/50 - txt_bounds.exactCenterY(), text);

            temp_text="Wi-Fi Quiz";
            text.getTextBounds(temp_text,0,temp_text.length(),txt_bounds);
            canvas.drawText(temp_text, screen_width/2 - txt_bounds.exactCenterX(), (3*screen_height)/4+button_wifi.getHeight()/2+screen_height/50 - txt_bounds.exactCenterY(), text);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event)
        {
            double x,y;
            double dist;
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    x=event.getX();
                    y=event.getY();
                    if(y<screen_height/2)
                    {
                        dist=Math.sqrt((x-screen_width/2)*(x-screen_width/2)+(y-screen_height/4)*(y-screen_height/4));
                        if(dist<=button_bluetooth.getWidth()/2)
                        {
                            intent = new Intent(Homescreen.this, Attendance.class);
                            Homescreen.this.startActivity(intent);
                        }
                    }
                    else
                    {
                        dist=Math.sqrt((x-screen_width/2)*(x-screen_width/2)+(y-(3*screen_height)/4)*(y-(3*screen_height)/4));
                        if(dist<=button_wifi.getWidth()/2)
                        {
                            intent = new Intent(Homescreen.this, WifiLandingPage.class);
                            Homescreen.this.startActivity(intent);
                        }
                    }

            }
            return super.onTouchEvent(event);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh)
        {
            screen_height= h;
            screen_width=w;
            button_bluetooth=Bitmap.createScaledBitmap(button_bluetooth,screen_height/4,screen_height/4,false);
            button_wifi=Bitmap.createScaledBitmap(button_wifi,screen_height/4,screen_height/4,false);
            text.setTextSize(screen_height/30);
        }
    }
}
*/