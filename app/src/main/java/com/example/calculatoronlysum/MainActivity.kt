package com.example.calculatoronlysum

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.calculatoronlysum.databinding.ActivityMainBinding
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var numberString = StringBuilder("0")
    private var equalsComplete: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val numberButtons = arrayOf(
            binding.buttonSayi0, binding.buttonSayi1, binding.buttonSayi2,
            binding.buttonSayi3, binding.buttonSayi4, binding.buttonSayi5,
            binding.buttonSayi6, binding.buttonSayi7, binding.buttonSayi8,
            binding.buttonSayi9, binding.buttonSayiDot
        )

        numberButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                if (index == 10) {
                    onDecimalClicked()
                } else {
                    onNumberClicked(index)
                }
            }
        }

        binding.buttonC.setOnClickListener { onButtonBackspaceClicked() }
        binding.buttonPlus.setOnClickListener { onPlusButtonClicked() }
        binding.buttonEqual.setOnClickListener { onButtonEqualClicked() }
        binding.buttonAC.setOnClickListener { onButtonResetClicked() }
    }

    private fun onNumberClicked(number: Int) {
        if (equalsComplete) {
            onButtonResetClicked()
        }
        if (numberString.toString() == "0") {
            numberString = StringBuilder(number.toString())
        }
        else {
            numberString.append(number)
        }
        updateTextView()
    }

    private fun onDecimalClicked() {
        if (equalsComplete) {
            onButtonResetClicked()
        }
        if (!numberString.endsWith(".")) {
            numberString.append(".")
        }
        updateTextView()
    }

    private fun updateTextView() {
        binding.textView.text = numberString.toString()
    }

    private fun onPlusButtonClicked() {
        if (numberString.endsWith(".")){
            numberString.deleteCharAt(numberString.length - 1)
        }

        if (!numberString.endsWith("+")) {
            equalsComplete = false
            numberString.append("+")
            updateTextView()
        }
    }

    private fun onButtonBackspaceClicked() {
        if (numberString.length > 1) {
            numberString.deleteCharAt(numberString.length - 1)
        } else {
            numberString = StringBuilder("0")
        }
        updateTextView()
    }

    private fun onButtonEqualClicked() {

        if (numberString.endsWith("+")) {
            numberString.deleteCharAt(numberString.length - 1)
            updateTextView()
            equalsComplete = true
        }
        if (numberString.contains("+")) {
            val numbers = numberString.split("+").map { it.toDouble() }
            val result = numbers.sum()
            Log.e("result", result.toString())
            if (result % 1 == 0.0) {
                numberString = StringBuilder(result.toInt().toString())
            } else {
                // Todo : example bug
                /*  16.1 + 0.1 = 16.2000000000000003 or 0.1 + 0.7 = 0.79999999999998

                    Hatanın nedeni, double türündeki sonucun, tam olarak ondalık sayı olarak
                gösterilmek üzere dönüştürülmesidir. Bu, IEEE 754 standartlarına göre işlenen
                kayan noktalı aritmetikten kaynaklanır.
                    BigDecimal sınıfını kullanarak sonucu yuvarlar. setScale işlevi ile kaç ondalık
                basamağa yuvarlanacağını belirleyebilirsiniz. Daha sonra, stripTrailingZeros()
                sonucu temizler.

                 */
                val roundedResult = BigDecimal(result).setScale(10, RoundingMode.HALF_EVEN).stripTrailingZeros()
                numberString = StringBuilder(roundedResult.toString())
            }

            updateTextView()
            equalsComplete = true
        }
    }

    private fun onButtonResetClicked() {
        numberString = StringBuilder("0")
        updateTextView()
        equalsComplete = false
    }
}
