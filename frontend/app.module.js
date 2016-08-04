(function () {
    'use strict';

    angular
        .module('app', []);

    angular
        .module('app')
        .config(['$httpProvider', function ($httpProvider) {
            $httpProvider.defaults.withCredentials = true;
        }]);

})();
