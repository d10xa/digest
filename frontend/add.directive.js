(function() {
    'use strict';

    angular
        .module('app')
        .directive('djtAdd', djtAdd);

    /* @ngInject */
    function djtAdd() {
        var directive = {
            restrict: 'E',
            templateUrl: '/add.html',
            scope: {
            },
            link: linkFunc,
            controller: Controller,
            controllerAs: 'vm',
            bindToController: true
        };

        return directive;

        function linkFunc(scope, el, attr, ctrl) {

        }
    }

    Controller.$inject = ['Digest'];

    /* @ngInject */
    function Controller(Digest) {
        var vm = this;

        activate();

        function activate() {
           vm.digest = {algo:'md5'};
           vm.schedule = function(digest){
             Digest.schedule(digest)
              .success(function(data){
                console.log(data);
                vm.error = null;
              })
              .error(function(data){
                console.log(data);
                vm.error = data;
              })
           };
        }
    }
})();
