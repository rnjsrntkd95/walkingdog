var mongoose = require('mongoose');
var Schema = mongoose.Schema;

const challengeSchema = new Schema({
    title: {
        type: String,
        required: true,
    },
    content: {
        type: String,  
    },
    start_date: {
        type: Date,
        required: true,
    },
    end_date: {
        type: Date,
        required: true,
    },
    date: {
        type: Number,
        default: 1,
    },
    population: {
        type: Number,
        required: true,
        default: 1,
    },
    populationLimit: {
        type: Number,
        required: true,
        default: 40,
    },
    producer: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true,
    },
    create_date: {
        type: Date,
        default: Date.now,
    },

})

module.exports = mongoose.model('Challenge', challengeSchema);