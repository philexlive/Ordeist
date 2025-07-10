package com.philexliveprojects.ordeist.ui

import androidx.annotation.StringRes
import com.philexliveprojects.ordeist.R

enum class Categories(@StringRes val label: Int) {
    PhotosForDocuments(R.string.photos_for_documents),
    Portraits(R.string.portraits),
    WeddingPhotos(R.string.wedding_photos),
    Editing(R.string.editing),
    DocumentsPrinting(R.string.documents_printing)
}

val ORDER_ID = "orderId"