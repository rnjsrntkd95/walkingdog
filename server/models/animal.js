var mongoose = require('mongoose');
var Schema = mongoose.Schema;

const animalSchema = new Schema({
    animalName: {
        type: String,
        required: true,
    },
    breed: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Breed',
        required: true,
    },
    birth: {
        type: Date,
        required: true,
    },
    weight: {
        type: Number,
        required: true,
    },
    gender: {
        type: String,
        default: "남",
    },
    user: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true,
    }
})

module.exports = mongoose.model('Animal', animalSchema);