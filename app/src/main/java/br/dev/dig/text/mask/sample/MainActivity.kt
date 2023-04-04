package br.dev.dig.text.mask.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.dev.dig.text.mask.TextMask
import br.dev.dig.text.mask.TextMaskTextWatcher
import br.dev.dig.text.mask.TextMultiMaskTextWatcher
import br.dev.dig.text.mask.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        TextMaskTextWatcher(binding.mainCard, TextMask("#### #### #### ####")).insert()
        TextMaskTextWatcher(binding.mainDate, TextMask("____-__-__", '_', true)).insert()
        TextMultiMaskTextWatcher(binding.mainMulti, listOf(
            TextMultiMaskTextWatcher.Option(TextMask("## ### ##")) { it.length < 8 },
            TextMultiMaskTextWatcher.Option(TextMask("##.##-####")) { it.length >= 8 },
        )).insert()
        TextMaskTextWatcher(binding.mainCustom, TextMask("!##-###==####")).insert()
    }
}