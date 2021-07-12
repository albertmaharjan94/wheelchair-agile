require('./configurations/setup')

const express = require("express")
const app = express();
const bodyParser = require('body-parser');
const cors = require('cors');


app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended : false,
}));


app.use("/public", express.static(__dirname + '/public'));

app.use('/api/auth', require('./routes/auth'));
app.use('/api/vehicle', require('./routes/vechile'));
app.use('/api/activity', require('./routes/activity'));
app.use('/api/tracker', require('./routes/tracker'));
app.use('/api/admin', require('./routes/adminAuth'));



app.use(function(err, req, res, next){
    res.status(422).send({error: err.message});
});
    
app.listen(process.env.PORT||3000,() => {
    console.log("Server is listening to port 3000")
})


