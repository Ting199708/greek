package com.example.tingyu.greek

import android.gesture.*
import android.media.AudioManager
import android.media.SoundPool
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.CompoundButton
import android.widget.TextView
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.graphics.Typeface
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class MainActivity : AppCompatActivity() , OnGesturePerformedListener {
    val greek = arrayOf("Α","Β","Γ","Δ","Ε","Ζ","Η","Θ","Ι","Κ","Λ","Μ","Ν","Ξ","Ο","Π","Ρ","Σ","Τ","Υ","Φ","Χ","Ψ","Ω")
    val greek_small = arrayOf("α","β","γ","δ","ε","ζ","η","θ","ι","κ","λ","μ","ν","ξ","ο","π","ρ","σ","τ","υ","φ","χ","ψ","ω")
    val pronunciation = arrayOf("alpha","beta","gamma","delta","epsilon","zeta","eta","theta","iota","kappa","lambda","mu","nu","xi","omicron","pi","rho","sigma","tau","upsilon","phi","chi","psi","omega")
    val ids = arrayOf(R.id.one,R.id.two,R.id.three,R.id.four,R.id.five,R.id.six,R.id.seven,R.id.eight,R.id.nine,R.id.ten,R.id.eleven,R.id.twelve,
            R.id.thirteen,R.id.fourteen,R.id.fifteen,R.id.sixteen,R.id.seventeen,R.id.eighteen,R.id.nineteen,R.id.twenty,R.id.twenty_one,R.id.twenty_two,
            R.id.twenty_three,R.id.twenty_four)
    val sounds = arrayOf(R.raw.alpha,R.raw.beta,R.raw.gamma,R.raw.delta,R.raw.epsilon,R.raw.zeta,R.raw.eta,R.raw.theta,R.raw.iota,R.raw.kappa,
            R.raw.lambda,R.raw.mu,R.raw.nu,R.raw.xi,R.raw.omicron,R.raw.pi,R.raw.rho,R.raw.sigma,R.raw.tau,R.raw.upsilon,R.raw.phi,R.raw.chi,
            R.raw.psi,R.raw.omega)
    var soundPool : SoundPool? = null
    var soundID = IntArray(24)
    var library : GestureLibrary
    var library2 : GestureLibrary
    var font: Typeface? = null

    init {
        library = GestureLibraries.fromRawResource(this, R.raw.gestures)
        library2 = GestureLibraries.fromRawResource(this, R.raw.gestures_1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        //發音
        soundPool = SoundPool(10, AudioManager.STREAM_MUSIC, 5)
        font = Typeface.createFromAsset(assets, "fonts/font.ttf")
        textView.typeface = font
        textView2.typeface = font
        for (i in 0..23) {
            soundID[i] = soundPool!!.load(this, sounds[i], 1)
        }
        for (i in 0..23) {
            val text : TextView = findViewById(ids[i])
            text.typeface = font
            text.text = greek_small[i]
        }
        for (i in 0..23) {
            val v = findViewById<TextView>(ids[i])
            v.setOnClickListener { view: View? ->
                textView.text = greek[i]+greek_small[i]
                textView2.text = pronunciation[i]
                textView4.visibility = View.GONE
                soundPool!!.play(soundID[i],1f,1f,0,0,1f)
            }
        }
        switch1.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {

            override fun onCheckedChanged(buttonView: CompoundButton,
                                 isChecked: Boolean) {
                if (isChecked) {
                    switch1.text = getString(R.string.switch_type2)
                    for (i in 0..23) {
                        val text : TextView = findViewById(ids[i])
                        text.text = greek[i]
                    }
                } else {
                    switch1.text = getString(R.string.switch_type1)
                    for (i in 0..23) {
                        val text : TextView = findViewById(ids[i])
                        text.text = greek_small[i]
                    }
                }
            }
        })

        (gestureOverlayView as GestureOverlayView).addOnGesturePerformedListener(this)
        gestureOverlayView.gestureStrokeType = GestureOverlayView.GESTURE_STROKE_TYPE_MULTIPLE
        library.load()
    }
    override fun onGesturePerformed(p0: GestureOverlayView?, p1: Gesture?) {

        val predictions = library.recognize(p1)
        textView4.visibility = View.GONE
        if (predictions.size > 0) {
            val prediction = predictions.get(0)
            if (prediction.score > 3) {
                val id = pronunciation.indexOf(prediction.name)
                textView.text = greek[id]+greek_small[id]
                textView2.text = pronunciation[id]
                soundPool!!.play(soundID[id],1f,1f,0,0,1f)
                Toast.makeText(this,prediction.name,Toast.LENGTH_SHORT).show()
                //Toast.makeText(this,prediction.score.toString(),Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this,getString(R.string.cannotread),Toast.LENGTH_SHORT).show()
                //Toast.makeText(this,prediction.score.toString(),Toast.LENGTH_SHORT).show()
            }
        }
    }
}
