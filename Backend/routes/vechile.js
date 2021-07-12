const express = require('express');
const router = express.Router();

const vehicleController = require('../controllers/vehicleContoller');

const vehicle = new vehicleController;

router.post('/', vehicle.addVehicle);
router.get('/:_id', vehicle.getVehicle);

module.exports = router;






