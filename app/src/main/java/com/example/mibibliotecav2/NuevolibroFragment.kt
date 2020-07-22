package com.example.mibibliotecav2
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.mibibliotecav2.model.remote.LibrosRemote
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_nuevolibro.*
import java.io.ByteArrayOutputStream

class NuevolibroFragment : Fragment() {
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myRef = database.getReference("libros")
    val idlibro = myRef.push().key

    private val REQUEST_IMAGE_ISBN = 1234
    private val REQUEST_IMAGE_PORTADA = 1111
    private var urlisbn = ""
    private var urlportada = ""
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val user = mAuth.currentUser
    val usrid = user?.uid

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nuevolibro, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        bt_examinarisbn.setOnClickListener{
            dispatchTakePictureIntent(REQUEST_IMAGE_ISBN)

        }
        bt_examinarportada.setOnClickListener{
            dispatchTakePictureIntent(REQUEST_IMAGE_PORTADA)

        }

        bt_guardar.setOnClickListener{
            val titulo = et_titulo.text.toString()
            val autor = et_autor.text.toString()
            val edicion = et_edicion.text.toString()
            val lugar = et_lugarpublicacion.text.toString()
            val year = et_anoedicion.text.toString()
            val paginas = et_numpaginas.text.toString()
            val genero = et_genero.text.toString()
            val notas = et_notas.text.toString()



                    if (titulo.isNotEmpty() && genero.isNotEmpty() && autor.isNotEmpty() && paginas.isNotEmpty()) {


                        val libro: LibrosRemote = LibrosRemote(
                            idlibro,
                            titulo,
                            autor,
                            edicion,
                            lugar,
                            year,
                            paginas,
                            genero,
                            notas,
                            urlisbn,
                            urlportada,
                            idlibro!!
                        )
                        myRef.child(usrid.toString()).child(idlibro).setValue(libro)
                        findNavController().navigate(R.id.action_nuevolibroFragment_to_bibliotecaPersonalFragment)
                    }


                    else

                    {
                        Toast.makeText(requireContext(), "llene todos los campos obligatorios", Toast.LENGTH_SHORT).show()
                        if (titulo.isEmpty())
                            et_titulo.setError("Este campo es obligatorio")
                        if (genero.isEmpty())
                            et_genero.setError("Este campo es obligatorio")
                        if (autor.isEmpty())
                            et_autor.setError("Este campo es obligatorio")
                        if (paginas.isEmpty())
                            et_numpaginas.setError("Este campo es obligatorio")
                }











        }


    }


    private fun dispatchTakePictureIntent(requestImage: Int) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also{ takePictureIntent->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also{
                startActivityForResult(takePictureIntent,requestImage)
            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(  resultCode == Activity.RESULT_OK){

            val mStorage = FirebaseStorage.getInstance()
            val id = myRef.push().key
            val photoRef = mStorage.reference.child(idlibro!!).child(id!!)
            val bitmap = data?.extras?.get("data") as Bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            var uploadTask = photoRef.putBytes(data)

            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                photoRef.downloadUrl }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if(requestCode == REQUEST_IMAGE_ISBN){
                        urlisbn = task.result.toString()
                        bt_examinarisbn.text = "Cargada"
                    }
                    else if (requestCode == REQUEST_IMAGE_PORTADA)
                    {
                        urlportada = task.result.toString()
                        bt_examinarportada.text = "Cargada"
                    }
                }
                }


        }

    }

}