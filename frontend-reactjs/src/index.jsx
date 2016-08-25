import React from 'react';
import {render} from 'react-dom';
import {Provider} from 'react-redux';
import {createStore} from 'redux';
import App from './App'

var counter = (state = 0, action) => {
    switch (action.type) {
        case 'INCREMENT_COUNTER':
            return state + 1;
        default:
            return state;
    }
};

let store = createStore(counter);

render(
    <Provider store={store}>
        <App />
    </Provider>,
    document.getElementById('root')
);
