package com.philexliveprojects.ordeist.ui

import androidx.annotation.StringRes
import com.philexliveprojects.ordeist.R

enum class Categories(@StringRes val label: Int) {
    Category1(R.string.photos_for_documents),
    Category2(R.string.portraits),
    Category3(R.string.wedding_photos),
    Category4(R.string.editing),
    Category5(R.string.documents_printing)
}
