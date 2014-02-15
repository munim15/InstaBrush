package com.example.ipa2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	TouchView me;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = new TouchView(this);
        setContentView(me);
        //Intent intent = new Intent(this, ImageActivity.class);
        //startActivity(intent);
        //Intent intent = getIntent();
		//index = intent.getIntExtra(ImageActivity.EXTRA_MESSAGE, 0);


    }
    int x = 1;
    int y = 0;
    boolean checked = true;
    Paint pTouch = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint pTouch2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    MenuItem viewMenu;
    
    int [] ids = {R.drawable.danbo, R.drawable.desert, R.drawable.tiger, R.drawable.henry};
    int index;
    boolean graysc;
    class TouchView extends View{
        Bitmap bgr, bgr2;
        Bitmap col;
        Bitmap overlayDefault;
        Bitmap overlay, overlay2, overlay3;
        //Paint pTouch;
        int X = -100;
        int Y = -100;
        Canvas c2, c3;
        Path path, path2;
        Bitmap curr;
        int width1;
        int height1;
        
        
        Bitmap shadowBitmap, mainBitmap, shadowBitmap2;
        Canvas shadow, shadow2;
        int [] shadowPixels, grayPixels, mainPixels, colPixels, shadowPixels2, blurPixels;


        public TouchView(Context context) {
            super(context);
            Intent intent = getIntent();
    		index = intent.getIntExtra(ImageActivity.EXTRA_MESSAGE, 0);
    		graysc = intent.getBooleanExtra(ImageActivity.EXTRA_MESSAGE2, true);
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            width1 = size.x;
            height1 = size.y;
            col = (rez(BitmapFactory.decodeResource(getResources(),ids[index]).copy(Config.ARGB_8888, true)));
            //bgr = rez(toGrayscale(BitmapFactory.decodeResource(getResources(),R.drawable.cop)).copy(Config.ARGB_8888, true));
            if (graysc) {
            bgr = rez(toGrayscale(BitmapFactory.decodeResource(getResources(),ids[index])).copy(Config.ARGB_8888, true));
            } else {
            bgr = rez(blur(BitmapFactory.decodeResource(getResources(),ids[index])).copy(Config.ARGB_8888, true));
            }
            //overlayDefault = BitmapFactory.decodeResource(getResources(),R.drawable.cop);
            //overlay = Bitmap.createBitmap(col.getWidth(), col.getHeight(), Config.ARGB_8888);
            overlay2 = Bitmap.createBitmap(width1, height1, Config.ARGB_8888);
            overlay3 = Bitmap.createBitmap(width1, height1, Config.ARGB_8888);
            //overlay = BitmapFactory.decodeResource(getResources(),R.drawable.cop).copy(Config.ARGB_8888, true);  
            overlay = col.copy(Config.ARGB_8888, true);
            //overlay3 = bgr.copy(Config.ARGB_8888, true);
            c2 = new Canvas(overlay);
            //c3 = new Canvas(bgr);
            c3 = new Canvas(overlay3);
            
            
            
            shadowBitmap = Bitmap.createBitmap(width1, height1, Config.ARGB_8888);
            shadow = new Canvas(shadowBitmap);
            shadowBitmap2 = Bitmap.createBitmap(width1, height1, Config.ARGB_8888);
            shadow2 = new Canvas(shadowBitmap2);
            mainBitmap = col.copy(Config.ARGB_8888, true);
            shadowPixels = new int[shadowBitmap.getWidth() * shadowBitmap.getHeight()];
            shadowPixels2 = new int[shadowBitmap2.getWidth() * shadowBitmap2.getHeight()];
            grayPixels = new int[bgr.getWidth() * bgr.getHeight()];
            colPixels = new int[col.getWidth() * col.getHeight()];
            mainPixels = new int[mainBitmap.getWidth() * mainBitmap.getHeight()];
            bgr.getPixels(grayPixels, 0, bgr.getWidth(), 0, 0, bgr.getWidth(), bgr.getHeight());
            col.getPixels(colPixels, 0, col.getWidth(), 0, 0, col.getWidth(), col.getHeight());
            
            
            //blurPixels = new int[bgr2.getWidth() * bgr2.getHeight()];
            //bgr2.getPixels(blurPixels, 0, bgr2.getWidth(), 0, 0, bgr2.getWidth(), bgr2.getHeight());
            
            path = new Path();
            path2 = new Path();
            if (android.os.Build.VERSION.SDK_INT >= 11) {
                setLayerType(View.LAYER_TYPE_SOFTWARE, null);
           }


            //pTouch = new Paint(Paint.ANTI_ALIAS_FLAG);
            pTouch.setStyle(Paint.Style.STROKE);
            pTouch.setAntiAlias(true);
            //pTouch.setXfermode(new PorterDuffXfermode(Mode.SRC));
            //pTouch.setAlpha(0);
            //pTouch.setColor(Color.TRANSPARENT);
            pTouch.setStrokeJoin(Paint.Join.ROUND);
    		pTouch.setStrokeCap(Paint.Cap.ROUND);
            pTouch.setStrokeWidth(20);
            pTouch.setColor(Color.BLACK);
            //pTouch.setMaskFilter(new BlurMaskFilter(10, Blur.NORMAL));
            
            
            pTouch2.setStyle(Paint.Style.STROKE);
            pTouch2.setAntiAlias(true);
            //pTouch.setXfermode(new PorterDuffXfermode(Mode.SRC));
            //pTouch.setAlpha(0);
            //pTouch.setColor(Color.TRANSPARENT);
            pTouch2.setStrokeJoin(Paint.Join.ROUND);
    		pTouch2.setStrokeCap(Paint.Cap.ROUND);
            pTouch2.setStrokeWidth(20);
            pTouch2.setColor(Color.RED);

        }
        
        
        
        
        
        public Bitmap toGrayscale(Bitmap bmpOriginal)
    	{        
    	    int width, height;
    	    height = bmpOriginal.getHeight();
    	    width = bmpOriginal.getWidth();    

    	    Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    	    Canvas c = new Canvas(bmpGrayscale);
    	    Paint paint = new Paint();
    	    ColorMatrix cm = new ColorMatrix();
    	    cm.setSaturation(0);
    	    ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
    	    paint.setColorFilter(f);
    	    c.drawBitmap(bmpOriginal, 0, 0, paint);
    	    return bmpGrayscale;
    	}
        
        public Bitmap blur(Bitmap bitmap) {
            

            // The bitmap we pass to gaussianBlur() needs to have a width
            // that's a power of 2, so scale up to 128x128.
            final int scaledSize = 128;
            bitmap = Bitmap.createScaledBitmap(bitmap,
                                               scaledSize, scaledSize,
                                               true);
            bitmap = rez(bitmap);
            return bitmap;
        }
        
        
        
        
        public Bitmap rez(Bitmap bm) {
    	    int width = bm.getWidth();
    	    int height = bm.getHeight();
    	    float scaleWidth = ((float) width1) / width;
    	    float scaleHeight = ((float) height1) / height;
    	    // CREATE A MATRIX FOR THE MANIPULATION
    	    Matrix matrix = new Matrix();
    	    // RESIZE THE BIT MAP
    	    matrix.postScale(scaleWidth, scaleHeight);

    	    // "RECREATE" THE NEW BITMAP
    	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    	    return resizedBitmap;
    	}
        
        private Bitmap overlayf(Bitmap bmp1, Bitmap bmp2) {
            Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
            Canvas canvas = new Canvas(bmOverlay);
            canvas.drawBitmap(bmp1, new Matrix(), null);
            canvas.drawBitmap(bmp2, new Matrix(), null);
            return bmOverlay;
        }


        @Override
        public boolean onTouchEvent(MotionEvent ev) {
        	int color = 0;
        	//path = new Path();
        	//path2 = new Path();
            switch (ev.getAction()) {

                case MotionEvent.ACTION_DOWN: {

                    X = (int) ev.getX();
                    Y = (int) ev.getY();
                    //color = bgr.getPixel(X, Y);
                    //color = (color << 24) & 0xFF000000;
                    //overlay.setPixel(X, Y, color);
                    if (checked) {
                    	shadowBitmap.eraseColor(Color.WHITE);
                		path.reset();
                    path.moveTo(X, Y);
                    } else {
                    	shadowBitmap.eraseColor(Color.WHITE);
                		path2.reset();
                    	path2.moveTo(X, Y);
                    }
                    invalidate();

                    break;
                }

                case MotionEvent.ACTION_MOVE: {

                        X = (int) ev.getX();
                        Y = (int) ev.getY();
                        //color = bgr.getPixel(X, Y);
                        //color = (color << 24) & 0xFF000000;
                        //overlay.setPixel(X, Y, color);
                        if(checked) {
                        	shadowBitmap.eraseColor(Color.WHITE);
                    		//path.reset();
                        path.lineTo(X, Y);
                        } else {
                        	shadowBitmap.eraseColor(Color.WHITE);
                    		//path2.reset();
                        	path2.lineTo(X, Y);
                        }
                        invalidate();
                        break;

                }           

                case MotionEvent.ACTION_UP:

                    break;

            }
            return true;
        }


        @Override
        public void onDraw(Canvas canvas){
        	
            super.onDraw(canvas);
            
            //draw background
            //if (checked) {
            //copy the default overlay into temporary overlay and punch a hole in it                          
            //c2.drawBitmap(overlayDefault, 0, 0, null); //exclude this line to show all as you draw
            //--c2.drawCircle(X, Y, 40, pTouch);
            //draw the overlay over the background  
            //c2.drawPath(path, pTouch);
            if (checked) {
            	if (x == 2) {
            		//shadowBitmap.eraseColor(Color.WHITE);
            		//path.reset();
            		//path2.reset();
            	}
            	x = 1;
            	shadow.drawPath(path, pTouch);
                shadowBitmap.getPixels(shadowPixels, 0, shadowBitmap.getWidth(), 0, 0, shadowBitmap.getWidth(), shadowBitmap.getHeight());
                mainBitmap.getPixels(mainPixels, 0, mainBitmap.getWidth(), 0, 0, mainBitmap.getWidth(), mainBitmap.getHeight());
                for (int i = 0; i < shadowPixels.length; i += 1) {
                	if (shadowPixels[i] == Color.BLACK) {
                		mainPixels[i] = grayPixels[i];
                	}
                }
                mainBitmap.setPixels(mainPixels, 0, mainBitmap.getWidth(), 0, 0, mainBitmap.getWidth(), mainBitmap.getHeight());
                canvas.drawBitmap(mainBitmap, 0, 0, null);
            } else if (y == 1) {
            	//shadow.drawPath(path, pTouch);
                //shadowBitmap.getPixels(shadowPixels, 0, shadowBitmap.getWidth(), 0, 0, shadowBitmap.getWidth(), shadowBitmap.getHeight());
                //mainBitmap.getPixels(mainPixels, 0, mainBitmap.getWidth(), 0, 0, mainBitmap.getWidth(), mainBitmap.getHeight());
                for (int i = 0; i < shadowPixels.length; i += 1) {
                		mainPixels[i] = colPixels[i];
                }
                mainBitmap.setPixels(mainPixels, 0, mainBitmap.getWidth(), 0, 0, mainBitmap.getWidth(), mainBitmap.getHeight());
                canvas.drawBitmap(mainBitmap, 0, 0, null);
            } else {
            	if (x == 2) {
            		//shadowBitmap.eraseColor(Color.WHITE);
            		//path2.reset();
            		//path.reset();
            	}
            	x = 1;
            	shadow.drawPath(path2, pTouch2);
                shadowBitmap.getPixels(shadowPixels, 0, shadowBitmap.getWidth(), 0, 0, shadowBitmap.getWidth(), shadowBitmap.getHeight());
                mainBitmap.getPixels(mainPixels, 0, mainBitmap.getWidth(), 0, 0, mainBitmap.getWidth(), mainBitmap.getHeight());
                for (int i = 0; i < shadowPixels.length; i += 1) {
                	if (shadowPixels[i] == Color.RED) {
                		mainPixels[i] = colPixels[i];
                	}
                }
                mainBitmap.setPixels(mainPixels, 0, mainBitmap.getWidth(), 0, 0, mainBitmap.getWidth(), mainBitmap.getHeight());
                canvas.drawBitmap(mainBitmap, 0, 0, null);
            	
            }
        }
        
        public Bitmap getBitmap() {
        	return mainBitmap;
        }
        
        public void share(Activity a){
    		Intent shareIntent = new Intent(Intent.ACTION_SEND);
    		shareIntent.setType("image/png");
    		String path = Environment.getExternalStorageDirectory().toString();
	    	  OutputStream fOut = null;
	    	  try {
	    	  File file = new File("/mnt/sdcard/", "sharing"+".jpg");
	    	  fOut = new FileOutputStream(file);

	    	  me.getBitmap().compress(Bitmap.CompressFormat.JPEG, 85, fOut);
	    	  fOut.flush();
	    	  fOut.close();
	    	  counter += 1;
	    	  MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
	    	  } catch (Exception e) {
	              Log.e("Error", e.toString());
	          }
    		File o = new File("/mnt/sdcard/", "sharing.JPEG");
    		shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(o));
    		
    		a.startActivity(Intent.createChooser(shareIntent , "Share options"));
    		
    	}


    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		viewMenu = menu.findItem(R.id.color);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	      case R.id.red:
	    	  checked = false;
	    	  if (graysc) {
	    	  Toast.makeText(this, "You have chosen the Grayscale to Color tool." ,
	    			  		Toast.LENGTH_SHORT).show();
	    	  } else {
	    		  Toast.makeText(this, "You have chosen the Sharpen tool." ,
	    			  		Toast.LENGTH_SHORT).show();
	    	  }
	    	  //paint.setColor(Color.RED); 
	    	  //color = Color.RED;
	    	  viewMenu.setIcon(R.drawable.eraser);
	    	  x = 2;
	    	  return true;
	      case R.id.black:
	    	  checked = true;
	    	  if(graysc) {
	    	  Toast.makeText(this, "You have chosen the Color to Grayscale tool.",
	    			  		Toast.LENGTH_SHORT).show();
	    	  } else {
	    		  Toast.makeText(this, "You have chosen the Blur tool.",
	    			  		Toast.LENGTH_SHORT).show();
	    	  }
	    	  //paint.setColor(Color.BLACK);
	    	  //color = Color.BLACK;
	    	  x = 2;
	    	  viewMenu.setIcon(R.drawable.draw);
	    	  return true;
	      case R.id.clear:
	    	  checked = false;
	    	  y = 1;
	    	  viewMenu.setIcon(R.drawable.clear);
	    	  Toast.makeText(this, "Are you sure you wan to Clear? Tap image if Yes, else select a different tool.",
  			  		Toast.LENGTH_LONG).show();
	    	  return true;
	      case R.id.small:
	    	  pTouch.setStrokeWidth(10);
	    	  pTouch2.setStrokeWidth(10);
	    	  return true;
	      case R.id.medium:
	    	  pTouch.setStrokeWidth(20);
	    	  pTouch2.setStrokeWidth(20);
	    	  return true;
	      case R.id.large:
	    	  pTouch.setStrokeWidth(50);
	    	  pTouch2.setStrokeWidth(50);
	    	  return true;
	      
	      case R.id.save:
	    	  String path = Environment.getExternalStorageDirectory().toString();
	    	  OutputStream fOut = null;
	    	  try {
	    	  File file = new File("/mnt/sdcard/", "Instabrush"+counter+".jpg");
	    	  fOut = new FileOutputStream(file);
	    	  Toast.makeText(this, "Your image has been saved!",
  			  		Toast.LENGTH_SHORT).show();
	    	  me.getBitmap().compress(Bitmap.CompressFormat.JPEG, 85, fOut);
	    	  fOut.flush();
	    	  fOut.close();
	    	  counter += 1;
	    	  MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
	    	  } catch (Exception e) {
	              Log.e("Error", e.toString());
	              Toast.makeText(this, "Save request not processed.",
	    			  		Toast.LENGTH_SHORT).show();
	          }
	    	  return true;
	      case R.id.share:
	    	  me.share(this);
	    	  return true;
	    	  
	      default:
	            return super.onOptionsItemSelected(item);
	      }
	}
	public static int counter = 0;



}
