const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const VehicleSchema =  new Schema({
    vehicle_type : {
        type : String,
        required : true,
    },
    vehicle_number : {
        type : Number,
        required : true,
    }
})

const Vehicle = mongoose.model('Vehicle', VehicleSchema);

module.exports = Vehicle;
