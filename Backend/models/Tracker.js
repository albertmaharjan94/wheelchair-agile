const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const TrackerSchema = new Schema({
    user : {
        type : Schema.Types.ObjectId,
        red : 'User',
        required : true
    },
    vehicle : {
        type : Schema.Types.ObjectId,
        red : 'Vehicle',
        required : true
    },
    location: {
        type: {
          type: String,
          enum: ['Point'], // 'location.type' must be 'Point'
          default: 'Point'
        },
        coordinates: {
          type: [Number],
          required: true
        }
    },
}, 
{
    timestamps: true
})

const Tracker = mongoose.model('Tracker', TrackerSchema);
module.exports = Tracker;
