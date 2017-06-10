'use strict';
 
angular.module('kohonenApp').controller('SearchController', ['HttpService', function(HttpService) {
    var self = this;
    
    self.image = null;
    self.res = null;
    self.search = search;
 
    function search() {
    	var fd = new FormData();
        var imgBlob = dataURItoBlob(self.image);
        fd.append('file', imgBlob);
        HttpService.search(fd)
        .then(
                function(d) {
                    self.res = d;
                },
                function(errResponse){
                    console.error('Error while searching');
                }
        );
    }
    
    function dataURItoBlob(dataURI) {
    	console.log(dataURI);
        var binary = atob(dataURI.split(',')[1]);
        var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];
        var array = [];
        for (var i = 0; i < binary.length; i++) {
          array.push(binary.charCodeAt(i));
        }
        return new Blob([new Uint8Array(array)], {
          type: mimeString
        });
      }
 
}]);