package edu.fudan.ringbell.view;
import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by niuzhenghao on 2018/1/7.
 */

public class MusicNameTextView extends android.support.v7.widget.AppCompatTextView {

        public MusicNameTextView(Context context) {
            super(context);
        }

        public MusicNameTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MusicNameTextView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }
        //返回textview是否处在选中的状态
        //而只有选中的textview才能够实现跑马灯效果
        @Override
        public boolean isFocused() {
            return true;
        }
}
