package associoholic.com.client.util

import android.text.TextWatcher
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.setConditionalError(conditions: List<Boolean>, error: String = "Required") =
        if (conditions.filter { it }.isEmpty()) this.error = null else this.error = error

fun EditText.addConditionalTextWather(textWather: TextWatcher) {
    if (!this.hasOnClickListeners()) this.addTextChangedListener(textWather)
}

fun String.isInt(): Boolean = try {
    this.toInt()
    true
} catch (exception: java.lang.Exception) {
    false
}
