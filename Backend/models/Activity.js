const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const ActivitySchema = new Schema({
    user : {
        type : Schema.Types.ObjectId,
        red : 'User',
        required : true
    },
    
    vehicle : {
        type : String,
         required : true
    },
    date : {
        type : String,
        required : false
    },
   activity : [
       {
    start_time : {
        type : String,
        required:  false,
    },
    end_time : {
        type : String,
        required : false
    },
    speed: {
        type : Number,
        required : false,
    },
    kilometer: {
        type : Number,
        required : false,
    }
   }],
   avg_speed : {
       type : Number,
       default : 0 
   },
   distance : {
       type  : Number,
       default : 0
   }

})

const Activity = mongoose.model('Activity', ActivitySchema);
module.exports = Activity;
