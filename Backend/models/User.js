const mongoose = require('mongoose');
const Schema = mongoose.Schema;


const UserSchema = new Schema({
    fullname: {
        type: String,
        required: true
    },
    email: {
        type: String,
        required: true
    },

    address: {
        type: String,
        required: true,
    },
    contact: {
        type: String,
        required: true,
    },

    emContact: {
        type: String,
        required: true,
    },

    age: {
        type: String,
        required: true,
    },

    password: {
        type: String,
        required: true,
    },
    vehicle : {
        type : Schema.Types.ObjectId,
        ref : 'Vehicle',
        required : true,
    },
    tokens: [{
        token: {
            type: String,
            required: true
        }
    }]


}, { timestamps: true });

const User = mongoose.model('User', UserSchema);
module.exports = User;
