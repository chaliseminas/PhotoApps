package np.com.chalise.photosapp

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import java.io.File

@Composable
fun ImageSelectionScreen() {

    val context = LocalContext.current
    val selectedImages = remember { mutableStateOf<List<Uri>>(emptyList()) }
    var text by remember { mutableStateOf(TextFieldValue(text = "50")) }

    val openGalleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uri ->
            uri.let {
                if (uri.size == 2) {
                    selectedImages.value = it
                } else {
                    Toast.makeText(context, "Please select two images.", Toast.LENGTH_LONG).show()
                }
            }
        }

    if (selectedImages.value.isNotEmpty()) {

        val newList = mutableListOf<Uri>()
        for (i in 1..text.text.toInt()) {
            newList.add(selectedImages.value[1])
        }

        for (i in 1..text.text.toInt()) {
            val index = i * (i + 1) / 2
            if (index < text.text.toInt()) {
                newList.removeAt(index)
                newList.add(index, selectedImages.value[0])
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Center
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(8.dp),
            ) {
                items(text.text.toInt()) {
                    Column {
                        Image(
                            painter = rememberAsyncImagePainter(model = newList[it]),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .padding(4.dp),
                        )
                        Text(
                            text = "Index: $it"
                        )
                    }
                }
            }
        }

    } else {
        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            contentAlignment = Center
        ) {
            Column {

                Text("Enter the number of list items to generate")

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = text,
                    onValueChange = { text = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { openGalleryLauncher.launch("image/*") },
                ) {
                    Text(text = "Pick Images")
                }
            }
        }
    }
}
