package com.shuai.memorypressure

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.nio.Buffer


class MainActivity : AppCompatActivity() {
    private lateinit var mHandler: Handler
    private lateinit var mTvAvaliableMemory: TextView
    private lateinit var mTvTotalMemory: TextView
    private lateinit var mSbMemory: SeekBar
    private lateinit var mBuffer : MutableList<MutableList<Byte>>

    private val mRefreshRunnable :Runnable = object : Runnable {
        override fun run() {
            refreshMemory()
            mHandler.postDelayed(this, DELAY)
        }
    }

    companion object {
        val TAG = MainActivity.javaClass.simpleName
        const val DELAY = 1000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mHandler = Handler()
        mBuffer = ArrayList()
        mSbMemory = findViewById(R.id.seekBar)
        mTvAvaliableMemory = findViewById(R.id.tv_avaliable_memory)
        mTvTotalMemory = findViewById(R.id.tv_total_memory)
        refreshMemory()
        mHandler.postDelayed(mRefreshRunnable, DELAY)
        mSbMemory.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                eatMemory(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }

    private fun refreshMemory() {
        val mi = ActivityManager.MemoryInfo()
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(mi)
        val availableMegs = mi.availMem / 0x100000L
        val total = mi.totalMem/0x100000L

        mTvAvaliableMemory.setText("可用内存:${availableMegs}M")
        mTvTotalMemory.setText("总内存:${total}M")
    }

    private fun eatMemory(progress:Int){
        val mi = ActivityManager.MemoryInfo()
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(mi)
        val total = mi.totalMem/0x100000L

        for (i in 0..50){
            try {
                mBuffer.add(ArrayList(1024 * 1024))
            }catch (e : Throwable){
                Log.e(TAG, e.message,e)
            }
        }


    }

}
