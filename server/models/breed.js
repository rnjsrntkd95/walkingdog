var mongoose = require('mongoose');
var Schema = mongoose.Schema;

const breedSchema = new Schema({
    breed: {
        type: String,
        required: true,
        unique: true,
        lowercase: true
    }
})

module.exports = mongoose.model('Breed', breedSchema);