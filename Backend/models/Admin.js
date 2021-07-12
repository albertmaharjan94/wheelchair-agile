const mongoose = require('mongoose');
const Schema = mongoose.Schema;


const AdminSchema = new Schema({
    email: {
        type: String,
        required: true
    },
    contact: {
        type: String,
        required: true,
    },
    role: {
        type: String,
        enum: ["ADMIN", "MANAGER"],
        required: true,
        default: "ADMIN"
    },
    password: {
        type: String,
        required: true,
    },
    tokens: [{
        token: {
            type: String,
            required: true
        }
    }]


}, { timestamps: true });

const Admin = mongoose.model('Admin', AdminSchema);
module.exports = Admin;
