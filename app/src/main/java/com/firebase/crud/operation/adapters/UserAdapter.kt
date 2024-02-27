import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.crud.operation.models.User
import com.firebase.crud.operations.R

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var userList: List<User> = listOf()
    private var editListener: OnEditClickListener? = null
    private var deleteListener: OnDeleteClickListener? = null
    private var viewListener: OnViewClickListener? = null

    fun setUserList(userList: List<User>) {
        this.userList = userList
        notifyDataSetChanged()
    }

    fun setOnEditClickListener(listener: OnEditClickListener) {
        this.editListener = listener
    }
    fun setOnViewClickListener(listener: OnViewClickListener) {
        this.viewListener = listener
    }

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        this.deleteListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.texName)
        private val updateData: ImageView = itemView.findViewById(R.id.update)
        private val deleteData: ImageView = itemView.findViewById(R.id.delete)

        fun bind(user: User) {
            name.text = user.name

            updateData.setOnClickListener {
                editListener?.onEditClick(user)
            }
            name.setOnClickListener {
                viewListener?.onViewClick(user)
            }

            deleteData.setOnClickListener {
                deleteListener?.onDeleteClick(user)
            }
        }
    }

    // Interfaces for click listeners
    interface OnEditClickListener {
        fun onEditClick(user: User)
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(user: User)
    }
    interface OnViewClickListener {
        fun onViewClick(user: User)
    }
}
